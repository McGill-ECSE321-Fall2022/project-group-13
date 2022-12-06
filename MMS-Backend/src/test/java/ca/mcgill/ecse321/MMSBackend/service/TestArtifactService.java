package ca.mcgill.ecse321.MMSBackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.model.*;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mona Kalaoun (m-kln)
 * The TestArtifactService class is responsible for testing the
 * business logic declared in ArtifactService.
 */
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

    private static final int R_ID = 2;
    private static final String R_NAME = "Room 2";
    private static final Room.RoomType R_TYPE = Room.RoomType.Small;

    private static final int A_KEY = 10;
    private static final String A_NAME = "Mona Lisa";
    private static final String A_DESCRIPTION = "Who painted the Mona Lisa, Mona Lisa haha? DaVinki?!";
    private static final String A_IMAGE = "images/monalisa.jpg";
    private static final Artifact.LoanStatus LOAN_STATUS = Artifact.LoanStatus.Unavailable;
    private static final double LOAN_FEE = 10000000.00;
    private static final boolean IS_DAMAGED = false;
    private static final double WORTH = 500000000.00;

    private static final MuseumManagementSystem system = createMms(MMS_ID,MMS_NAME,OPEN_TIME,CLOSE_TIME,MAX_LOAN_NUMBER,TICKET_FEE);
    private static final List<MuseumManagementSystem> ALL_MMS = Arrays.asList(system);

    private static final Room r1 = createRoom(3,"Room 3", Room.RoomType.Small,system);
    private static final Room r2 = createRoom(4,"Room 4", Room.RoomType.Large,system);
    private static final Room r3 = createRoom(5,"Room 5", Room.RoomType.Storage,system);

    private static final Artifact a1 = createArtifact(2,"La Joconde", "Mona Lisa but French",
            "images/monalisa.jpg", Artifact.LoanStatus.Available, 5, false, 1000, 3, 1 );

    private static final Artifact a2 = createArtifact(7,"Orange Couple", "Two-Sided",
            "images/orange.PNG", Artifact.LoanStatus.Loaned, 100, true, 100000, 4, 1 );

    private static final Artifact a3 = createArtifact(4,"Cat", "A royal cat",
            "images/Cat.PNG", Artifact.LoanStatus.Unavailable, 10, false, 100, 5, 1 );

    private final List<Artifact> ALL_ARTIFACTS = Arrays.asList(a1,a2,a3);

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
                    } else if (invocation.getArgument(0).equals(3)){
                        Room room = new Room();
                        room.setRoomId(3);
                        room.setName("Room 3");
                        room.setType(Room.RoomType.Small);
                        room.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
                        return room;
                    } else if (invocation.getArgument(0).equals(4)){
                        Room room = new Room();
                        room.setRoomId(4);
                        room.setName("Room 4");
                        room.setType(Room.RoomType.Large);
                        room.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
                        return room;
                    }else {
                        return null;
                    }
                });
        lenient().when(artifactDao.findArtifactByArtifactId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(A_KEY)) {
                Artifact artifact1 = new Artifact();

                artifact1.setArtifactId(A_KEY);
                artifact1.setName(A_NAME);
                artifact1.setImage(A_IMAGE);
                artifact1.setLoanStatus(LOAN_STATUS);
                artifact1.setLoanFee(LOAN_FEE);
                artifact1.setIsDamaged(IS_DAMAGED);
                artifact1.setWorth(WORTH);
                artifact1.setRoomLocation(roomDao.findRoomByRoomId(R_ID));
                artifact1.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
                return artifact1;
            } else {
                return null;
            }
        });

        lenient().when(artifactDao.findAll()).thenReturn(ALL_ARTIFACTS);
        lenient().when(mmsDao.findAll()).thenReturn(ALL_MMS);

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
        Artifact a = null;
        try {
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH, R_ID);
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
    }

    @Test
    public void testCreateArtifactNull() {
        String error = null;
        Artifact a = null;
        try {
            a = service.createArtifact(null, null, null, null, 0, false, 0,
                    0);
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
            a = service.createArtifact(name, description, image, null, 0, false, 0, 0);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(a);
        assertEquals("No null values are accepted.", error);

    }

    @Test
    public void testCreateArtifactNegativeLoanFee() {
        double fee = LOAN_FEE * -1;
        Artifact a = null;
        String error = null;

        try {
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, fee, IS_DAMAGED, WORTH, R_ID);
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
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, worth, R_ID);
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
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH, roomId);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("The room id can not be negative.", error);
    }

    @Test
    public void testCreateArtifactInNonExistingRoom(){
        int roomId = 9;
        Artifact a = null;
        String error = null;

        try{
            a = service.createArtifact(A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH, roomId);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("The room with id: " + roomId + " was not found.", error);
    }


    @Test
    public void testEditArtifact() {
        Artifact.LoanStatus status = Artifact.LoanStatus.Available;

        Artifact a = null;
        try {
            a = service.editArtifact(A_KEY,A_NAME, A_DESCRIPTION, A_IMAGE, status, LOAN_FEE, IS_DAMAGED, WORTH,
                    R_ID);
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
    }

    @Test
    public void testEditArtifactNegativeArtifactId() {
        int a_id = A_KEY * -1;
        Artifact a = null;
        String error = null;

        try {
            a = service.editArtifact(a_id, A_NAME, A_DESCRIPTION, A_IMAGE, LOAN_STATUS, LOAN_FEE, IS_DAMAGED, WORTH,
                    R_ID);
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
                    R_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Artifact with id: " + a_id + " was not found.", error);
    }

    @Test
    public void testDeleteArtifact(){
        boolean result = false;

        try {
            result= service.deleteArtifact(A_KEY);
        }catch (MuseumManagementSystemException e){
            fail();
        }

        assertTrue(result);
    }

    @Test
    public void testDeleteNullArtifact(){
        int aId = 12;
        String error = "";

        try {
            service.deleteArtifact(aId);
        }catch (MuseumManagementSystemException e){
            error+=e.getMessage();
        }

        assertEquals("Artifact with id: " + aId + " was not found.",error);
    }

    @Test
    public void testGetAllArtifacts(){
        List<Artifact> artifacts = service.getAllArtifacts();
        assertEquals(ALL_ARTIFACTS, artifacts);

    }

    @Test
    public void testGetArtifact(){
        Artifact a = null;
        try {
            a = service.getArtifact(A_KEY);
        } catch (MuseumManagementSystemException e) {
            fail();
        }
        assertNotNull(a);

    }

    @Test
    public void testGetNullArtifact(){
        int aId = 12;
        Artifact a = null;
        String error = "";
        try {
            a = service.getArtifact(aId);
        } catch (MuseumManagementSystemException e) {
            error += e.getMessage();
        }
        assertEquals("Artifact with id: " + aId + " was not found.", error);
    }

    @Test
    public void testGetAllArtifactsByRoomType(){
        List<Artifact> aSmall = service.getAllArtifactsByRoomType(Room.RoomType.Small);
        List<Artifact> aLarge = service.getAllArtifactsByRoomType(Room.RoomType.Large);
        List<Artifact> aStorage = service.getAllArtifactsByRoomType(Room.RoomType.Storage);
        assertNotNull(aSmall);
        assertNotNull(aLarge);
        assertNotNull(aStorage);
        assertEquals(1, aSmall.size());
        assertEquals(1, aLarge.size());
        assertEquals(1, aStorage.size());
    }

    @Test
    public void testGetAllArtifactsByRoomId(){
        List<Artifact> room3 = service.getAllArtifactsByRoomId(3);
        List<Artifact> room4 = service.getAllArtifactsByRoomId(4);
        List<Artifact> room5 = service.getAllArtifactsByRoomId(5);
        assertNotNull(room3);
        assertNotNull(room4);
        assertNotNull(room5);
        assertEquals(1, room3.size());
        assertEquals(1, room4.size());
        assertEquals(1, room5.size());
    }

    @Test
    public void testGetAllArtifactsByLoanStatus(){
        List<Artifact> u = service.getAllArtifactsByLoanStatus(Artifact.LoanStatus.Unavailable);
        List<Artifact> a = service.getAllArtifactsByLoanStatus(Artifact.LoanStatus.Available);
        List<Artifact> l = service.getAllArtifactsByLoanStatus(Artifact.LoanStatus.Loaned);
        assertNotNull(u);
        assertNotNull(a);
        assertNotNull(l);
        assertEquals(1, u.size());
        assertEquals(1, a.size());
        assertEquals(1, l.size());
    }

    @Test
    public void testGetAllArtifactsByState(){
        List<Artifact> notDamaged = service.getAllArtifactsByState(false);
        List<Artifact> damaged = service.getAllArtifactsByState(true);
        assertNotNull(notDamaged);
        assertNotNull(damaged);
        assertEquals(2, notDamaged.size());
        assertEquals(1, damaged.size());
    }


    /**
     * Private helper method for creating a Museum Management System
     * @param id
     * @param name
     * @param openTime
     * @param closeTime
     * @param maxLoanNumber
     * @param ticketFee
     * @return a MuseumManagementSystem object
     */

    private static MuseumManagementSystem createMms(int id, String name, Time openTime, Time closeTime,
                                                    int maxLoanNumber, double ticketFee) {
        MuseumManagementSystem mms = new MuseumManagementSystem();
        mms.setSystemId(id);
        mms.setName(name);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);
        return mms;
    }


    /**
     * Private helper method for creating a room
     * @param id
     * @param name
     * @param roomType
     * @param mms
     * @return a room object
     */
    private static Room createRoom(int id, String name, Room.RoomType roomType, MuseumManagementSystem mms) {
        Room room = new Room();
        room.setRoomId(id);
        room.setName(name);
        room.setType(roomType);
        room.setMuseumManagementSystem(mms);
        return room;
    }

    /**
     * Private helper method for creating an artifact
     * @param id
     * @param name
     * @param description
     * @param image
     * @param status
     * @param loanFee
     * @param isDamaged
     * @param worth
     * @param roomId
     * @param mmsId
     * @return an artifact object
     */
    private static Artifact createArtifact(int id, String name, String description, String image, Artifact.LoanStatus
            status, int loanFee, boolean isDamaged, double worth, int roomId, int mmsId) {
        Artifact artifact = new Artifact();
        artifact.setArtifactId(id);
        artifact.setName(name);
        artifact.setImage(image);
        artifact.setLoanStatus(status);
        artifact.setDescription(description);
        artifact.setIsDamaged(isDamaged);
        artifact.setWorth(worth);
        artifact.setRoomLocation(Room.getWithRoomId(roomId));
        artifact.setMuseumManagementSystem(MuseumManagementSystem.getWithSystemId(mmsId));
        return artifact;
    }



}



