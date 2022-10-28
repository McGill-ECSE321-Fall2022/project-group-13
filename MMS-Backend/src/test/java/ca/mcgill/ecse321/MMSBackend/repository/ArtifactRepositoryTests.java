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
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;
import ca.mcgill.ecse321.MMSBackend.model.Artifact.LoanStatus;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;

/**
 * @author Nazia Chowdhury
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ArtifactRepositoryTests {

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private RoomRepository roomRepository;

    @AfterEach
    public void clearDatabase() {
        // Delete artifact first to avoid violating not-null constraints
        artifactRepository.deleteAll();
        roomRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadArtifact() {
        // Create a museum management system
        MuseumManagementSystem mms = new MuseumManagementSystem();
        String museumName = "Artifact's MMS";
        Time openTime = Time.valueOf("8:00:00");
        Time closeTime = Time.valueOf("16:00:00");
        int maxLoanNumber = 5;
        double ticketFee = 13.25;
 
        mms.setName(museumName);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);
 
        // Save the museum management system to the database
        mmsRepository.save(mms);

        // Create storage room
        Room room = new Room();
        RoomType type = RoomType.Storage;
        String roomName = "RoomRoom"; 

        room.setType(type);
        room.setMuseumManagementSystem(mms);
        room.setName(roomName);

        // Save the room to the database
        roomRepository.save(room);

        // Create an artifact
        Artifact artifact = new Artifact();
        String artifactName = "Test Artifact";
        String image = "png";
        String description = "Artifact Description";
        double worth = 50;
        boolean aIsDamaged = false;
        double aLoanFee = 55;
        LoanStatus status = LoanStatus.Available;

        artifact.setName(artifactName);
        artifact.setImage(image);
        artifact.setDescription(description);
        artifact.setWorth(worth);
        artifact.setMuseumManagementSystem(mms);
        artifact.setRoomLocation(room);
        artifact.setIsDamaged(aIsDamaged);
        artifact.setLoanFee(aLoanFee);
        artifact.setLoanStatus(status);
        artifact.setRoomLocation(room);

        // Save the artifact to the database
        artifactRepository.save(artifact);

        // Check that everything was saved in the database

        // Get ids of artifact, room and museum management system
        Integer mmsId = mms.getSystemId();
        Integer artifactId = artifact.getArtifactId();
        Integer roomId = artifact.getRoomLocation().getRoomId();

        // Make the variables null
        mms = null;
        artifact = null;
        
        // Get artifact from database
        artifact = artifactRepository.findArtifactByArtifactId(artifactId);

        // Assert values
        assertNotNull(artifact);

        // Check if that artifact has the correct attributes
        assertEquals(artifactId, artifact.getArtifactId());
        assertEquals(artifactName, artifact.getName());
        assertEquals(image, artifact.getImage());
        assertEquals(description, artifact.getDescription());
        assertEquals(worth, artifact.getWorth());
        assertEquals(aIsDamaged, artifact.getIsDamaged());
        assertEquals(aLoanFee, artifact.getLoanFee());
        assertEquals(status, artifact.getLoanStatus());

        assertNotNull(artifact.getMuseumManagementSystem());
        assertEquals(mmsId, artifact.getMuseumManagementSystem().getSystemId());
        assertEquals(museumName, artifact.getMuseumManagementSystem().getName());
        assertEquals(openTime, artifact.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, artifact.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, artifact.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, artifact.getMuseumManagementSystem().getTicketFee());

        assertNotNull(artifact.getRoomLocation());
        assertEquals(roomId, artifact.getRoomLocation().getRoomId());
        assertEquals(type, artifact.getRoomLocation().getType());
        assertEquals(roomName, artifact.getRoomLocation().getName());
        assertEquals(mmsId, artifact.getRoomLocation().getMuseumManagementSystem().getSystemId());
    }
}
