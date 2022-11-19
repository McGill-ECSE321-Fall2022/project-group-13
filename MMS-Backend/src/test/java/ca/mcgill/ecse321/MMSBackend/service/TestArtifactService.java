package ca.mcgill.ecse321.MMSBackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.model.*;

import java.nio.file.InvalidPathException;
import java.sql.Time;

@ExtendWith(MockitoExtension.class)
public class TestArtifactService {
    @Mock
    private ArtifactRepository artifactDao;

    @Mock
    private RoomRepository roomDao;

    @Mock
    private MuseumManagementSystemRepository mmsDao;

    @InjectMocks
    private ArtifactService service;

    private static final int MMS_ID = 1;
    private static final String MMS_NAME = "Le Louvre";
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final int MAX_LOAN_NUMBER = 5;
    private static final double TICKET_FEE = 25.0;

    private static final int R_ID = 5;
    private static final String R_NAME = "DaVinki Room";
    private static final Room.RoomType R_TYPE = Room.RoomType.Small;

    private static final int A_KEY = 10;
    private static final String A_NAME = "Mona Lisa";
    private static final String A_DESCRIPTION = "Who painted the Mona Lisa, Mona Lisa haha? DaVinki?!";
    private static final String A_IMAGE = "C:\\Users\\mona_\\project-group-13\\MMS-Backend\\ClassDiagram.png";
    private static final Artifact.LoanStatus LOAN_STATUS = Artifact.LoanStatus.Unavailable;
    private static final double LOAN_FEE = 10000000.00;
    private static final boolean IS_DAMAGED = false;
    private static final double WORTH = 500000000.00;


    @BeforeEach
    public void setMockOutput() {
        lenient().when(mmsDao.findMuseumManagementSystemBySystemId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(MMS_ID)) {
                        MuseumManagementSystem mms = new MuseumManagementSystem();
                        mms.setSystemId(MMS_ID);
                        mms.setName(MMS_NAME);
                        mms.setOpenTime(OPEN_TIME);
                        mms.setCloseTime(CLOSE_TIME);
                        mms.setMaxLoanNumber(MAX_LOAN_NUMBER);
                        mms.setTicketFee(TICKET_FEE);
                        return mms;
                    } else {
                        return null;
                    }
                });
        lenient().when(roomDao.findRoomByRoomId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(R_ID)) {
                        Room room = new Room();
                        room.setRoomId(R_ID);
                        room.setName(R_NAME);
                        room.setType(R_TYPE);
                        room.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
                        return room;
                    } else {
                        return null;
                    }
                });
        lenient().when(artifactDao.findArtifactByArtifactId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(A_KEY)) {
                Artifact artifact = new Artifact();

                artifact.setArtifactId(A_KEY);
                artifact.setName(A_NAME);
                artifact.setImage(A_IMAGE);
                artifact.setLoanStatus(LOAN_STATUS);
                artifact.setLoanFee(LOAN_FEE);
                artifact.setIsDamaged(IS_DAMAGED);
                artifact.setWorth(WORTH);
                artifact.setRoomLocation(roomDao.findRoomByRoomId(R_ID));
                artifact.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
                return artifact;
            } else {
                return null;
            }
        });

        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };

        lenient().when(mmsDao.save(any(MuseumManagementSystem.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(roomDao.save(any(Room.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(artifactDao.save(any(Artifact.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateArtifact() {
        assertEquals(0, service.getAllArtifacts().size());
        Artifact a = null;
        try {
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH, R_ID, MMS_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(a);
        assertEquals(A_NAME, a.getName());
        assertEquals(A_DESCRIPTION, a.getDescription());
        assertEquals(A_IMAGE, a.getImage());
        assertEquals(LOAN_STATUS, a.getLoanStatus());
        assertEquals(LOAN_FEE, a.getLoanFee());
        assertEquals(IS_DAMAGED, a.getIsDamaged());
        assertEquals(WORTH, a.getWorth());
        assertEquals(R_ID, a.getRoomLocation().getRoomId());
        assertEquals(MMS_ID, a.getMuseumManagementSystem().getSystemId());
    }

    @Test
    public void testCreateArtifactNull() {
        String error = null;
        Artifact a = null;
        try {
            a = service.createArtifact(null, null, null, null, 0, false, 0,
                    0, 0);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(a);
        assertEquals("No null values are accepted.", error);
    }

    @Test
    public void testCreateArtifactEmpty() {
        String name = " ";
        String description = "I am the best.";
        String image = " ";
        String error = null;
        Artifact a = null;
        try {
            a = service.createArtifact(name, description, image, null, 0, false, 0, 0, 0);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(a);
        assertEquals("No null values are accepted.", error);

    }

    @Test
    public void testCreateArtifactNonExistentImage() {
        String image = "MonaLisa.jpg";
        Artifact a = null;
        String error = null;

        try {
            a = service.createArtifact(A_NAME, A_DESCRIPTION, image, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH, R_ID, MMS_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Image file does not exist.", error);
    }

    @Test
    public void testCreateArtifactNegativeLoanFee() {
        double fee = LOAN_FEE * -1;
        Artifact a = null;
        String error = null;

        try {
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, fee, IS_DAMAGED, WORTH, R_ID, MMS_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("The loan fee can not be a negative value.", error);
    }

    @Test
    public void testCreateArtifactNegativeWorth() {
        double worth = WORTH * -1;
        Artifact a = null;
        String error = null;

        try {
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, worth, R_ID, MMS_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("The monetary value of the artifact can not be negative.", error);
    }

    @Test
    public void testCreateArtifactNegativeRoomId() {
        int roomId = R_ID * -1;
        Artifact a = null;
        String error = null;

        try {
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH, roomId, MMS_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("The room id can not be negative.", error);
    }

    @Test
    public void testCreateArtifactNegativeMmsId() {
        int mmsId = MMS_ID * -1;
        Artifact a = null;
        String error = null;

        try {
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH, R_ID, mmsId);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("The system id can not be negative.", error);
    }


    @Test
    public void testEditArtifact() {
        Artifact.LoanStatus status = Artifact.LoanStatus.Available;

        Artifact a = null;
        try {
            a = service.editArtifact(A_KEY,A_NAME, A_DESCRIPTION, A_IMAGE, status, LOAN_FEE, IS_DAMAGED, WORTH,
                    R_ID, MMS_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(a);
        assertEquals(A_KEY, a.getArtifactId());
        assertEquals(A_NAME, a.getName());
        assertEquals(A_DESCRIPTION, a.getDescription());
        assertEquals(A_IMAGE, a.getImage());
        assertEquals(status, a.getLoanStatus());
        assertEquals(LOAN_FEE, a.getLoanFee());
        assertEquals(IS_DAMAGED, a.getIsDamaged());
        assertEquals(WORTH, a.getWorth());
        assertEquals(R_ID, a.getRoomLocation().getRoomId());
        assertEquals(MMS_ID, a.getMuseumManagementSystem().getSystemId());
    }

    @Test
    public void testEditArtifactNegativeArtifactId() {
        int a_id = A_KEY * -1;
        Artifact a = null;
        String error = null;

        try {
            a = service.editArtifact(a_id, A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH,
                    R_ID, MMS_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Artifact id can not be negative.", error);
    }

    @Test
    public void testEditNonExistentArtifact(){
        int a_id = 3;
        Artifact a = null;
        String error = null;

        try {
            a = service.editArtifact(a_id, A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH,
                    R_ID, MMS_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Artifact with id: " + a_id + " was not found.", error);
    }

    @Test
    public void testDeleteArtifact(){
        boolean result = service.deleteArtifact(A_KEY);
        assertTrue(result);
    }





}



