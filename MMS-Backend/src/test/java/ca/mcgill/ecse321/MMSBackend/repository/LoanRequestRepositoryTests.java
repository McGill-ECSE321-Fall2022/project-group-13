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
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.LoanRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;

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

    @Autowired
    private RoomRepository roomRepository;

    @AfterEach
    public void clearDatabase() {
        // Delete the loan requests first to avoid violating not-null constraints
        loanRequestRepository.deleteAll();

        artifactRepository.deleteAll();
        roomRepository.deleteAll();
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadLoanRequest() {

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

        // Create storage room
        Room room = new Room();
        RoomType type = RoomType.Storage;

        room.setType(type);
        room.setMuseumManagementSystem(mms);

        // Save the room to the database
        roomRepository.save(room);

        artifact.setName(artifactName);
        artifact.setImage(image);
        artifact.setDescription(description);
        artifact.setWorth(worth);
        artifact.setMuseumManagementSystem(mms);
        artifact.setRoomLocation(room);

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
        client.setMuseumManagementSystem(mms);

        // Save the client to the database
        clientRepository.save(client);

        // Create a loan request
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setArtifact(artifact);
        loanRequest.setClient(client);
        loanRequest.setMuseumManagementSystem(mms);

        // Save the loan request to the database
        loanRequestRepository.save(loanRequest);

        // Check that everything was saved in the database

        // Get ids of artifacts, clients, and donation requests
        Integer mmsId = mms.getSystemId();
        Integer artifactId = artifact.getArtifactId();
        Integer roomId = artifact.getRoomLocation().getRoomId();
        String clientUsername = client.getUsername();
        Integer loanRequestId = loanRequest.getRequestId();

        // Make the variables null
        mms = null;
        artifact = null;
        client = null;
        loanRequest = null;
        room = null;

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
        assertEquals(mmsId, loanRequest.getArtifact().getMuseumManagementSystem().getSystemId());

        assertNotNull(loanRequest.getArtifact().getRoomLocation());
        assertEquals(roomId, loanRequest.getArtifact().getRoomLocation().getRoomId());
        assertEquals(type, loanRequest.getArtifact().getRoomLocation().getType());
        assertEquals(mmsId, loanRequest.getArtifact().getRoomLocation().getMuseumManagementSystem().getSystemId());

        assertNotNull(loanRequest.getClient());
        assertEquals(clientUsername, loanRequest.getClient().getUsername());
        assertEquals(clientName, loanRequest.getClient().getName());
        assertEquals(clientPassword, loanRequest.getClient().getPassword());
        assertEquals(currentLoanNumber, loanRequest.getClient().getCurrentLoanNumber());
        assertEquals(mmsId, loanRequest.getClient().getMuseumManagementSystem().getSystemId());

        assertNotNull(loanRequest.getMuseumManagementSystem());
        assertEquals(mmsId, loanRequest.getMuseumManagementSystem().getSystemId());
        assertEquals(name, loanRequest.getMuseumManagementSystem().getName());
        assertEquals(openTime, loanRequest.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, loanRequest.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, loanRequest.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, loanRequest.getMuseumManagementSystem().getTicketFee());
        assertEquals(mmsId, loanRequest.getMuseumManagementSystem().getSystemId());
    }
}
