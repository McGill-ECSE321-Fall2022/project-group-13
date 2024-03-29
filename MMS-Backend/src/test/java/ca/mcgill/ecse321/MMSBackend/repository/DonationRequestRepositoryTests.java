package ca.mcgill.ecse321.MMSBackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.DonationRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;
import ca.mcgill.ecse321.MMSBackend.model.DonationRequest.DonationStatus;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;

/**
 * @author Yu An Lu
 */
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private SpecificWeekDayRepository specificWeekDayRepository;

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    public void clearDatabase() {
        // Delete the donation requests first to avoid violating not-null constraints
        donationRequestRepository.deleteAll();

        artifactRepository.deleteAll();
        roomRepository.deleteAll();
        specificWeekDayRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @AfterEach
    public void clearDatabaseAfter() {
        // Delete the donation requests first to avoid violating not-null constraints
        donationRequestRepository.deleteAll();

        artifactRepository.deleteAll();
        roomRepository.deleteAll();
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadDonationRequest() {
        // Create a museum management system
        MuseumManagementSystem mms = new MuseumManagementSystem();

        String mmsName = "Donation's MMS";
        Time openTime = Time.valueOf("9:00:00");
        Time closeTime = Time.valueOf("17:00:00");
        int maxLoanNumber = 5;
        double ticketFee = 13.25;

        mms.setName(mmsName);
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

        // Create storage room
        Room room = new Room();
        RoomType type = RoomType.Storage;
        String roomName =  "Storage Room 1";

        room.setType(type);
        room.setName(roomName);
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
        String username = "Donationuse";
        String clientName = "Leonardo Da Vinci";
        String clientPassword = "123456";
        int currentLoanNumber = 1;

        client.setUsername(username);
        client.setName(clientName);
        client.setPassword(clientPassword);
        client.setCurrentLoanNumber(currentLoanNumber);
        client.setMuseumManagementSystem(mms);

        // Save the client to the database
        clientRepository.save(client);

        // Create a donation request
        DonationRequest donationRequest = new DonationRequest();
        DonationStatus status = DonationStatus.Pending;
        donationRequest.setStatus(status);
        donationRequest.setArtifact(artifact);
        donationRequest.setClient(client);
        donationRequest.setMuseumManagementSystem(mms);

        // Save the donation request to the database
        donationRequestRepository.save(donationRequest);

        // Check that everything was saved in the database

        // Get ids of artifacts, clients, and donation requests
        Integer mmsId = mms.getSystemId();
        Integer artifactId = artifact.getArtifactId();
        Integer roomId = artifact.getRoomLocation().getRoomId();
        String clientUsername = client.getUsername();
        Integer donationRequestId = donationRequest.getRequestId();

        // Make the variables null
        mms = null;
        artifact = null;
        room = null;
        client = null;
        donationRequest = null;

        // Get donationRequest from database
        donationRequest = donationRequestRepository.findDonationRequestByRequestId(donationRequestId);

        // Assert values
        assertNotNull(donationRequest);
        assertEquals(donationRequestId, donationRequest.getRequestId());
        assertEquals(status, donationRequest.getStatus());

        assertNotNull(donationRequest.getArtifact());
        assertEquals(artifactId, donationRequest.getArtifact().getArtifactId());
        assertEquals(artifactName, donationRequest.getArtifact().getName());
        assertEquals(image, donationRequest.getArtifact().getImage());
        assertEquals(description, donationRequest.getArtifact().getDescription());
        assertEquals(worth, donationRequest.getArtifact().getWorth());
        assertEquals(mmsId, donationRequest.getArtifact().getMuseumManagementSystem().getSystemId());

        assertNotNull(donationRequest.getArtifact().getRoomLocation());
        assertEquals(roomId, donationRequest.getArtifact().getRoomLocation().getRoomId());
        assertEquals(type, donationRequest.getArtifact().getRoomLocation().getType());
        assertEquals(roomName, donationRequest.getArtifact().getRoomLocation().getName());
        assertEquals(mmsId, donationRequest.getArtifact().getRoomLocation().getMuseumManagementSystem().getSystemId());

        assertNotNull(donationRequest.getClient());
        assertEquals(clientUsername, donationRequest.getClient().getUsername());
        assertEquals(clientName, donationRequest.getClient().getName());
        assertEquals(clientPassword, donationRequest.getClient().getPassword());
        assertEquals(currentLoanNumber, donationRequest.getClient().getCurrentLoanNumber());
        assertEquals(mmsId, donationRequest.getClient().getMuseumManagementSystem().getSystemId());

        assertNotNull(donationRequest.getMuseumManagementSystem());
        assertEquals(mmsId, donationRequest.getMuseumManagementSystem().getSystemId());
        assertEquals(mmsName, donationRequest.getMuseumManagementSystem().getName());
        assertEquals(openTime, donationRequest.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, donationRequest.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, donationRequest.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, donationRequest.getMuseumManagementSystem().getTicketFee());
        assertEquals(mmsId, donationRequest.getMuseumManagementSystem().getSystemId());
    }
}
