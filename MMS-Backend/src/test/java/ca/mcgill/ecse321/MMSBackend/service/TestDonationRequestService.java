package ca.mcgill.ecse321.MMSBackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.sql.Time;
import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;

@ExtendWith(MockitoExtension.class)
public class TestDonationRequestService {

    @Mock
    private DonationRequestRepository donationRequestRepository;
    @Mock
    private ArtifactRepository artifactRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private MuseumManagementSystemRepository mmsRepository;

    @InjectMocks
    private DonationRequestService service;

    private static final int DONATION_REQUEST_ID = 1;

    private static final int ARTIFACT_ID = 2;
    private static final String ARTIFACT_NAME = "Mona Lisa";
    private static final String ARTIFACT_IMAGE = "Mona_Lisa.jpg";
    private static final String ARTIFACT_DESCRIPTION = "Woman looking at the viewer";
    private static final boolean ARTIFACT_IS_DAMAGED = false;
    private static final double ARTIFACT_WORTH = 1000000.0;

    private static final int STORAGE_ROOM_ID = 3;
    private static final String STORAGE_ROOM_NAME = "Storage Room";
    private static final RoomType STORAGE_ROOM_TYPE = RoomType.Storage;
    private static final int SMALL_ROOM_ID = 4;
    private static final String SMALL_ROOM_NAME = "Small Room 1";
    private static final RoomType SMALL_ROOM_TYPE = RoomType.Small;

    private static final String CLIENT_USERNAME = "Leonardo Da Vinci Username";
    private static final String CLIENT_NAME = "Leonardo Da Vinci";
    private static final String CLIENT_PASSWORD = "I painted Mona Lisa";
    private static final int CLIENT_CURRENT_LOAN_NUMBER = 0;

    private static final int MMS_ID = 5;
    private static final String MMS_NAME = "Louvre";
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final int MAX_LOAN_NUMBER = 5;
    private static final double TICKET_FEE = 10.0;

    private static final int NONEXISTING_ID = 0;
    private static final String NONEXISTING_USERNAME = "I don't exist";

    @BeforeEach
	public void setMockOutput() {
        lenient().when(mmsRepository.findMuseumManagementSystemBySystemId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
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
        lenient().when(roomRepository.findRoomByRoomId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(STORAGE_ROOM_ID)) {
                        Room room = new Room();
                        room.setRoomId(STORAGE_ROOM_ID);
                        room.setName(STORAGE_ROOM_NAME);
                        room.setType(STORAGE_ROOM_TYPE);
                        room.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        return room;
                    } else if (invocation.getArgument(0).equals(SMALL_ROOM_ID)) {
                        Room room = new Room();
                        room.setRoomId(SMALL_ROOM_ID);
                        room.setName(SMALL_ROOM_NAME);
                        room.setType(SMALL_ROOM_TYPE);
                        room.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        return room;
                    } else {
                        return null;
                    }
        });
        lenient().when(artifactRepository.findArtifactByArtifactId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(ARTIFACT_ID)) {
                        Artifact artifact = new Artifact();
                        artifact.setArtifactId(ARTIFACT_ID);
                        artifact.setName(ARTIFACT_NAME);
                        artifact.setImage(ARTIFACT_IMAGE);
                        artifact.setDescription(ARTIFACT_DESCRIPTION);
                        artifact.setIsDamaged(ARTIFACT_IS_DAMAGED);
                        artifact.setWorth(ARTIFACT_WORTH);
                        artifact.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        return artifact;
                    } else {
                        return null;
                    }
        });
        lenient().when(clientRepository.findClientByUsername(anyString()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(CLIENT_USERNAME)) {
                        Client client = new Client();
                        client.setUsername(CLIENT_USERNAME);
                        client.setName(CLIENT_NAME);
                        client.setPassword(CLIENT_PASSWORD);
                        client.setCurrentLoanNumber(CLIENT_CURRENT_LOAN_NUMBER);
                        client.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        return client;
                    } else {
                        return null;
                    }
        });
        lenient().when(donationRequestRepository.findDonationRequestByRequestId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(DONATION_REQUEST_ID)) {
                        DonationRequest donationRequest = new DonationRequest();
                        donationRequest.setRequestId(DONATION_REQUEST_ID);
                        donationRequest.setArtifact(artifactRepository.findArtifactByArtifactId(ARTIFACT_ID));
                        donationRequest.setClient(clientRepository.findClientByUsername(CLIENT_USERNAME));
                        donationRequest.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        return donationRequest;
                    } else {
                        return null;
                    }
        });

        // Whenever anything is saved, just return the parameter object
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};

        lenient().when(donationRequestRepository.save(any(DonationRequest.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(artifactRepository.save(any(Artifact.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(roomRepository.save(any(Room.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(clientRepository.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(mmsRepository.save(any(MuseumManagementSystem.class))).thenAnswer(returnParameterAsAnswer);
    
    }

    @Test
    public void testCreateDonationArtifact(){
        assertEquals(0, service.getAllDonationRequests().size());

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(ARTIFACT_NAME, ARTIFACT_IMAGE, ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(artifact);
        checkCreatedDonationArtifact(artifact);
    }

    public void checkCreatedDonationArtifact(Artifact artifact){
        assertEquals(ARTIFACT_NAME, artifact.getName());
        assertEquals(ARTIFACT_IMAGE, artifact.getImage());
        assertEquals(ARTIFACT_DESCRIPTION, artifact.getDescription());
        assertEquals(ARTIFACT_IS_DAMAGED, artifact.getIsDamaged());
        assertEquals(ARTIFACT_WORTH, artifact.getWorth());
        assertEquals(MMS_ID, artifact.getMuseumManagementSystem().getSystemId());
    }

    @Test
    public void testCreateDonationArtifactNullMms(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(ARTIFACT_NAME, ARTIFACT_IMAGE, ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, null);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("MuseumManagementSystem does not exist", error);

    }

    @Test
    public void testCreateDonationArtifactNullName(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(null, ARTIFACT_IMAGE, ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationArtifactNullImage(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(ARTIFACT_NAME, null, ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationArtifactNullDescription(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(ARTIFACT_NAME, ARTIFACT_IMAGE, null, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationArtifactEmptyName(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact("", ARTIFACT_IMAGE, ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationArtifactEmptyImage(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(ARTIFACT_NAME, "", ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationArtifactEmptyDescription(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(ARTIFACT_NAME, ARTIFACT_IMAGE, "", ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationArtifactSpaceName(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(" ", ARTIFACT_IMAGE, ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationArtifactSpaceImage(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(ARTIFACT_NAME, " ", ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationArtifactSpaceDescription(){
        String error = null;

        Artifact artifact = null;
        try {
            artifact = service.createDonationArtifact(ARTIFACT_NAME, ARTIFACT_IMAGE, " ", ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(artifact);
        assertEquals("Empty fields not allowed", error);

    }

    @Test
    public void testCreateDonationRequest(){
        DonationRequest donationRequest = null;
        try {
            donationRequest = service.createDonationRequest(clientRepository.findClientByUsername(CLIENT_USERNAME), artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(donationRequest);
        assertEquals(DonationRequest.DonationStatus.Pending, donationRequest.getStatus());
        checkCreatedDonationRequest(donationRequest);
        
    }

    private void checkCreatedDonationRequest(DonationRequest donationRequest){
        assertEquals(CLIENT_USERNAME, donationRequest.getClient().getUsername());
        assertEquals(CLIENT_NAME, donationRequest.getClient().getName());
        assertEquals(CLIENT_PASSWORD, donationRequest.getClient().getPassword());
        assertEquals(CLIENT_CURRENT_LOAN_NUMBER, donationRequest.getClient().getCurrentLoanNumber());
        assertEquals(MMS_ID, donationRequest.getClient().getMuseumManagementSystem().getSystemId());

        checkCreatedDonationArtifact(donationRequest.getArtifact());

        assertEquals(MMS_ID, donationRequest.getMuseumManagementSystem().getSystemId());
        assertEquals(MMS_NAME, donationRequest.getMuseumManagementSystem().getName());
        assertEquals(OPEN_TIME, donationRequest.getMuseumManagementSystem().getOpenTime());
        assertEquals(CLOSE_TIME, donationRequest.getMuseumManagementSystem().getCloseTime());
        assertEquals(MAX_LOAN_NUMBER, donationRequest.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(TICKET_FEE, donationRequest.getMuseumManagementSystem().getTicketFee());
    }

    @Test
    public void testCreateDonationRequestNullClient(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.createDonationRequest(null, artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Client does not exist", error);

    }

    @Test
    public void testCreateDonationRequestNullArtifact(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.createDonationRequest(clientRepository.findClientByUsername(CLIENT_USERNAME), null, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Artifact does not exist", error);

    }

    @Test
    public void testCreateDonationRequestNullMMS(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.createDonationRequest(clientRepository.findClientByUsername(CLIENT_USERNAME), artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), null);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Museum Management System does not exist", error);

    }

    @Test
    public void testCreateDonationRequestAndClientDoesNotExist(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.createDonationRequest(clientRepository.findClientByUsername(NONEXISTING_USERNAME), artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Client does not exist", error);

    }

    @Test
    public void testCreateDonationRequestAndArtifactDoesNotExist(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.createDonationRequest(clientRepository.findClientByUsername(CLIENT_USERNAME), artifactRepository.findArtifactByArtifactId(NONEXISTING_ID), mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Artifact does not exist", error);

    }

    @Test
    public void testCreateDonationRequestAndMmsDoesNotExist(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.createDonationRequest(clientRepository.findClientByUsername(CLIENT_USERNAME), artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), mmsRepository.findMuseumManagementSystemBySystemId(NONEXISTING_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Museum Management System does not exist", error);
        
    }

    @Test
    public void testDeletePendingDonationRequest(){
        String error = null;
        try {
            service.deleteRejectedDonationRequest(DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Cannot delete a donation request that is pending", error);
    }

    @Test
    public void testRejectDonationRequest(){
        DonationRequest donationRequest = null;
        try {
            donationRequest = service.rejectDonationRequest(DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(donationRequest);
        assertEquals(DonationRequest.DonationStatus.Rejected, donationRequest.getStatus());
        checkCreatedDonationRequest(donationRequest);
    }

    @Test
    public void testRejectDonationRequestAndDonationRequestDoesNotExist(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.rejectDonationRequest(NONEXISTING_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Donation request not found", error);
    }

    @Test
    public void testDeleteRejectedDonationRequest(){
        try {
            service.deleteRejectedDonationRequest(DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNull(donationRequestRepository.findDonationRequestByRequestId(DONATION_REQUEST_ID));
        assertNull(artifactRepository.findArtifactByArtifactId(ARTIFACT_ID));
        assertNotNull(clientRepository.findClientByUsername(CLIENT_USERNAME));
        assertNotNull(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
    }

    @Test
    public void testApproveDonationRequest(){
        service.createDonationArtifact(ARTIFACT_NAME, ARTIFACT_IMAGE, ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, null);
        service.createDonationRequest(clientRepository.findClientByUsername(CLIENT_USERNAME), artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        
        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(STORAGE_ROOM_ID));
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(donationRequest);
        assertEquals(DonationRequest.DonationStatus.Approved, donationRequest.getStatus());
        checkApprovedDonationRequest(donationRequest);
    }       

    public void checkApprovedDonationRequest(DonationRequest donationRequest){
       checkCreatedDonationRequest(donationRequest);
       assertEquals(STORAGE_ROOM_ID, donationRequest.getArtifact().getRoomLocation());
       assertEquals(Artifact.LoanStatus.Unavailable, donationRequest.getArtifact().getLoanStatus());
    }

    @Test
    public void testApproveDonationRequestAndDonationRequestDoesNotExist(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(NONEXISTING_ID, roomRepository.findRoomByRoomId(STORAGE_ROOM_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Donation request not found", error);
    }

    @Test
    public void testApproveDonationRequestAndRequestAlreadyApproved(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(STORAGE_ROOM_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Donation request already approved", error);
    }

    @Test
    public void testApproveDonationRequestNullRoom(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(DONATION_REQUEST_ID, null);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Selected room does not exist", error);
    }

    @Test
    public void testApproveDonationRequestAndRoomDoesNotExist(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(NONEXISTING_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Selected room does not exist", error);
    }

    @Test
    public void testApproveDonationRequestAndRoomIsNotStorageRoom(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(SMALL_ROOM_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Selected room is not a storage room", error);
    }


    @Test
    public void testGetExistingDonationRequest(){
        DonationRequest donationRequest = null;
        try {
            donationRequest = service.getDonationRequest(DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(donationRequest);
        checkCreatedDonationRequest(donationRequest);
    }

    @Test
    public void testGetNonExistingDonationRequest(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.getDonationRequest(NONEXISTING_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Donation request not found", error);
    }
    
    @Test
    public void testGetAllDonationRequests(){
        List<DonationRequest> donationRequests = null;
        donationRequests = service.getAllDonationRequests();

        assertNotNull(donationRequests);
        assertEquals(1, donationRequests.size());
        checkCreatedDonationRequest(donationRequests.get(0));
    }

    @Test
    public void testGetAllDonationRequestsByStatus(){
        List<DonationRequest> donationRequests = null;
        donationRequests = service.getAllDonationRequestsByStatus(DonationRequest.DonationStatus.Approved);

        assertNotNull(donationRequests);
        assertEquals(1, donationRequests.size());
        checkCreatedDonationRequest(donationRequests.get(0));
    }

    @Test
    public void testDeleteApprovedDonationRequest(){
        String error = null;

        try {
            service.deleteRejectedDonationRequest(DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Cannot delete a donation request that has been approved", error);
    }

    @Test
    public void testDeleteNonExistingDonationRequest(){
        String error = null;

        try {
            service.deleteRejectedDonationRequest(NONEXISTING_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Donation request not found", error);
    }
    
}