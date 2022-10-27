package ca.mcgill.ecse321.MMSBackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.dao.ArtifactRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.LoanRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoanRequestRepositoryTests {
    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @AfterEach
    public void clearDatabase() {
        // Delete the loan requests first to avoid violating not-null constraints
        loanRequestRepository.deleteAll();

        artifactRepository.deleteAll();
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadDonationRequest() {

        // Create a museum management system
        MuseumManagementSystem mms = new MuseumManagementSystem();
        String name = "MMS";
        Time openTime = Time.valueOf("8:00:00");
        Time closeTime = Time.valueOf("16:00:00");
        int maxLoanNumber = 5;
        double ticketFee = 13.25;

        mms.setName(name);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);

        // Save the museum management system to the database
        mmsRepository.save(mms);

        // Create an artifact
        Artifact artifact = new Artifact();
        String artifactName = "Test Artifact";
        String image = "png";
        String description = "Artifact Description";
        double worth = 50;

        artifact.setName(artifactName);
        artifact.setImage(image);
        artifact.setDescription(description);
        artifact.setWorth(worth);

        // Save the artifact to the database
        artifactRepository.save(artifact);

        // Create a client
        Client client = new Client();
        String username = "Test Client";
        String clientName = "ClientName ClientLastName";
        String clientPassword = "IamClient";
        int currentLoanNumber = 1;

        client.setUsername(username);
        client.setName(clientName);
        client.setPassword(clientPassword);
        client.setCurrentLoanNumber(currentLoanNumber);

        // Save the client to the database
        clientRepository.save(client);

        // Create a loan request
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setArtifact(artifact);
        loanRequest.setClient(client);

        // Save the loan request to the database
        loanRequestRepository.save(loanRequest);

        // Check that everything was saved in the database

        // Get ids of artifacts, clients, and donation requests
        String mmsId = mms.getSystemId();
        String artifactId = artifact.getArtifactId();
        String clientUsername = client.getUsername();
        String loanRequestId = loanRequest.getRequestId();

        // Make the variables null
        mms = null;
        artifact = null;
        client = null;
        loanRequest = null;

        // Get loan request from database
        loanRequest = loanRequestRepository.findLoanRequestByRequestId(loanRequestId);

        // Assert values
        assertNotNull(loanRequest);
        assertEquals(loanRequestId, loanRequest.getRequestId());

        assertNotNull(loanRequest.getArtifact());
        assertEquals(artifactId, loanRequest.getArtifact().getArtifactId());
        assertEquals(artifactName, loanRequest.getArtifact().getName());
        assertEquals(image, loanRequest.getArtifact().getImage());
        assertEquals(description, loanRequest.getArtifact().getDescription());
        assertEquals(worth, loanRequest.getArtifact().getWorth());

        assertNotNull(loanRequest.getClient());
        assertEquals(clientUsername, loanRequest.getClient().getUsername());
        assertEquals(clientName, loanRequest.getClient().getName());
        assertEquals(clientPassword, loanRequest.getClient().getPassword());
        assertEquals(currentLoanNumber, loanRequest.getClient().getCurrentLoanNumber());

        assertNotNull(loanRequest.getMuseumManagementSystem());
        assertEquals(mmsId, loanRequest.getMuseumManagementSystem().getSystemId());
        assertEquals(name, loanRequest.getMuseumManagementSystem().getName());
        assertEquals(openTime, loanRequest.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, loanRequest.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, loanRequest.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, loanRequest.getMuseumManagementSystem().getTicketFee());
    }
}
