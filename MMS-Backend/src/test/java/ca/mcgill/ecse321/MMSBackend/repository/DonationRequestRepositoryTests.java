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
import ca.mcgill.ecse321.MMSBackend.dao.DonationRequestRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.DonationRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DonationRequestRepositoryTests {
    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @AfterEach
    public void clearDatabase() {
        // Delete the donation requests first to avoid violating not-null constraints
        donationRequestRepository.deleteAll();

        artifactRepository.deleteAll();
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadDonationRequest() {
        // Create a museum management system
        MuseumManagementSystem mms = new MuseumManagementSystem();
        Time openTime = new Time(9, 0, 0);
        Time closeTime = new Time(17, 0, 0);
        int maxLoanNumber = 5;
        double ticketFee = 13.25;

        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);

        // Save the museum management system to the database
        mmsRepository.save(mms);

        // Create an artifact
        Artifact artifact = new Artifact();
        String artifactName = "Mona Lisa";
        String image = "png";
        String description = "Woman looking at the viewer";
        double worth = 1000000000;

        artifact.setName(artifactName);
        artifact.setImage(image);
        artifact.setDescription(description);
        artifact.setWorth(worth);

        // Save the artifact to the database
        artifactRepository.save(artifact);

        // Create a client
        Client client = new Client();
        String clientName = "Leonardo Da Vinci";
        String clientPassword = "123456";
        int currentLoanNumber = 1;

        client.setName(clientName);
        client.setPassword(clientPassword);
        client.setCurrentLoanNumber(currentLoanNumber);

        // Save the client to the database
        clientRepository.save(client);

        // Create a donation request
        DonationRequest donationRequest = new DonationRequest();
        donationRequest.setArtifact(artifact);
        donationRequest.setClient(client);

        // Save the donation request to the database
        donationRequestRepository.save(donationRequest);

        // Check that everything was saved in the database

        // Get ids of artifacts, clients, and donation requests
        String artifactId = artifact.getArtifactId();
        String clientUsername = client.getUsername();
        String donationRequestId = donationRequest.getRequestId();

        // Make the variables null
        mms = null;
        artifact = null;
        client = null;
        donationRequest = null;

        // Get donationRequest from database
        donationRequest = donationRequestRepository.findDonationRequestByRequestId(donationRequestId);

        // Assert values
        assertNotNull(donationRequest);
        assertEquals(donationRequestId, donationRequest.getRequestId());

        assertNotNull(donationRequest.getArtifact());
        assertEquals(artifactId, donationRequest.getArtifact().getArtifactId());
        assertEquals(artifactName, donationRequest.getArtifact().getName());
        assertEquals(image, donationRequest.getArtifact().getImage());
        assertEquals(description, donationRequest.getArtifact().getDescription());
        assertEquals(worth, donationRequest.getArtifact().getWorth());

        assertNotNull(donationRequest.getClient());
        assertEquals(clientUsername, donationRequest.getClient().getUsername());
        assertEquals(clientName, donationRequest.getClient().getName());
        assertEquals(clientPassword, donationRequest.getClient().getPassword());
        assertEquals(currentLoanNumber, donationRequest.getClient().getCurrentLoanNumber());

        assertNotNull(donationRequest.getMuseumManagementSystem());
        assertEquals(openTime, donationRequest.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, donationRequest.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, donationRequest.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, donationRequest.getMuseumManagementSystem().getTicketFee());
    }
}
