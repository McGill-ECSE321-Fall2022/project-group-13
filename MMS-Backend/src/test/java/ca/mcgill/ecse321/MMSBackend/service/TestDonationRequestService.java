package ca.mcgill.ecse321.MMSBackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

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
import ca.mcgill.ecse321.MMSBackend.model.DonationRequest.DonationStatus;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;

/**
 * @author Yu An Lu (yu-an-lu)
 * 
 * The TestDonationRequestService class tests the business logic declared in
 * DonationRequestService.
 * 
 */
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

    private static final int PENDING_DONATION_REQUEST_ID = 2;
    private static final int APPROVED_DONATION_REQUEST_ID = 3;
    private static final int REJECTED_DONATION_REQUEST_ID = 4;

    private static final int ARTIFACT_ID = 5;
    private static final String ARTIFACT_NAME = "Mona Lisa";
    private static final String ARTIFACT_IMAGE = "Mona_Lisa.jpg";
    private static final String ARTIFACT_DESCRIPTION = "Woman looking at the viewer";
    private static final boolean ARTIFACT_IS_DAMAGED = false;
    private static final double ARTIFACT_WORTH = 1000000.0;

    private static final int STORAGE_ROOM_ID = 6;
    private static final String STORAGE_ROOM_NAME = "Storage Room";
    private static final RoomType STORAGE_ROOM_TYPE = RoomType.Storage;
    private static final int SMALL_ROOM_ID = 7;
    private static final String SMALL_ROOM_NAME = "Small Room 1";
    private static final RoomType SMALL_ROOM_TYPE = RoomType.Small;

    private static final String CLIENT_USERNAME = "LeonardoDaVinciUsername";
    private static final String CLIENT_NAME = "Leonardo Da Vinci";
    private static final String CLIENT_PASSWORD = "I painted Mona Lisa";
    private static final int CLIENT_CURRENT_LOAN_NUMBER = 3;

    private static final int MMS_ID = 8;
    private static final String MMS_NAME = "Louvre";
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final int MAX_LOAN_NUMBER = 5;
    private static final double TICKET_FEE = 10.0;

    private static final int NONEXISTING_ID = 0;
    private static final String NONEXISTING_USERNAME = "I don't exist";

    private static final MuseumManagementSystem MMS = createMms(MMS_ID, MMS_NAME, OPEN_TIME, CLOSE_TIME, MAX_LOAN_NUMBER, TICKET_FEE);
    private static final Client CLIENT_A = createClient("a", "A", "aA", 2, MMS);


    private static final DonationRequest PENDING_DONATION_REQUEST = createDonationRequest(PENDING_DONATION_REQUEST_ID, CLIENT_A, createArtifact(20, "a0", "png0", "0", false, 10.0, MMS), DonationStatus.Pending, MMS);
    private static final DonationRequest APPROVED_DONATION_REQUEST = createDonationRequest(APPROVED_DONATION_REQUEST_ID, createClient("b", "B", "bB", 1, MMS), createArtifact(21, "a1", "png1", "1", false, 11.0, MMS), DonationStatus.Approved, MMS);

    private static final List<DonationRequest> ALL_DONATION_REQUESTS = Arrays.asList(
            PENDING_DONATION_REQUEST,
            APPROVED_DONATION_REQUEST
    );

    @BeforeEach
	public void setMockOutput() {
        lenient().when(mmsRepository.findMuseumManagementSystemBySystemId(anyInt()))
            .thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(MMS_ID)) {
                return MMS;
            } else {
                return null;
            }
        });
        lenient().when(roomRepository.findRoomByRoomId(anyInt()))
            .thenAnswer((InvocationOnMock invocation) -> {
                if (invocation.getArgument(0).equals(STORAGE_ROOM_ID)) {
                    Room room = createRoom(STORAGE_ROOM_ID, STORAGE_ROOM_NAME, STORAGE_ROOM_TYPE, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                    return room;
                } else if (invocation.getArgument(0).equals(SMALL_ROOM_ID)) {
                    Room room = createRoom(SMALL_ROOM_ID, SMALL_ROOM_NAME, SMALL_ROOM_TYPE, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                    return room;
                } else {
                    return null;
                }
        });
        lenient().when(artifactRepository.findArtifactByArtifactId(anyInt()))
            .thenAnswer((InvocationOnMock invocation) -> {
                if (invocation.getArgument(0).equals(ARTIFACT_ID)) {
                    Artifact artifact = createArtifact(ARTIFACT_ID, ARTIFACT_NAME, ARTIFACT_IMAGE, ARTIFACT_DESCRIPTION, ARTIFACT_IS_DAMAGED, ARTIFACT_WORTH, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                    return artifact;
                } else {
                    return null;
                }
        });
        lenient().when(clientRepository.findClientByUsername(anyString()))
            .thenAnswer((InvocationOnMock invocation) -> {
                if (invocation.getArgument(0).equals(CLIENT_USERNAME)) {
                    Client client = createClient(CLIENT_USERNAME, CLIENT_NAME, CLIENT_PASSWORD, CLIENT_CURRENT_LOAN_NUMBER, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                    return client;
                } else {
                    return null;
                }
        });
        lenient().when(donationRequestRepository.findDonationRequestByRequestId(anyInt()))
            .thenAnswer((InvocationOnMock invocation) -> {
               if (invocation.getArgument(0).equals(PENDING_DONATION_REQUEST_ID)) {
                    DonationRequest donationRequest = createDonationRequest(PENDING_DONATION_REQUEST_ID, clientRepository.findClientByUsername(CLIENT_USERNAME), artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), DonationRequest.DonationStatus.Pending, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                    return donationRequest;
                } else if (invocation.getArgument(0).equals(APPROVED_DONATION_REQUEST_ID)) {
                    DonationRequest donationRequest = createDonationRequest(APPROVED_DONATION_REQUEST_ID, clientRepository.findClientByUsername(CLIENT_USERNAME), artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), DonationRequest.DonationStatus.Approved, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                    return donationRequest;
                } else if (invocation.getArgument(0).equals(REJECTED_DONATION_REQUEST_ID)) {
                    DonationRequest donationRequest = createDonationRequest(REJECTED_DONATION_REQUEST_ID, clientRepository.findClientByUsername(CLIENT_USERNAME), artifactRepository.findArtifactByArtifactId(ARTIFACT_ID), DonationRequest.DonationStatus.Rejected, mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
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
        
        // Whenever findAll() is involved, return a list of all donation requests
        lenient().when(donationRequestRepository.findAll()).thenReturn(ALL_DONATION_REQUESTS);
    
    }

    @Test
    public void testCreateDonationArtifact(){
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
        assertEquals(CLIENT_NAME, donationRequest.getClient().getName());
        assertEquals(CLIENT_PASSWORD, donationRequest.getClient().getPassword());
        assertEquals(CLIENT_CURRENT_LOAN_NUMBER, donationRequest.getClient().getCurrentLoanNumber());

        checkCreatedDonationArtifact(donationRequest.getArtifact());

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
    public void testApproveDonationRequest(){
        
        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(PENDING_DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(STORAGE_ROOM_ID));
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(donationRequest);
        assertEquals(DonationRequest.DonationStatus.Approved, donationRequest.getStatus());
        checkApprovedDonationRequest(donationRequest);
    }       

    public void checkApprovedDonationRequest(DonationRequest donationRequest){
       checkCreatedDonationRequest(donationRequest);
       assertEquals(STORAGE_ROOM_NAME, donationRequest.getArtifact().getRoomLocation().getName());
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
            donationRequest = service.approveDonationRequest(APPROVED_DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(STORAGE_ROOM_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Donation request already approved", error);
    }

    @Test
    public void testApproveDonationRequestAndRequestAlreadyRejected(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(REJECTED_DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(STORAGE_ROOM_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Donation request already rejected", error);
    }

    @Test
    public void testApproveDonationRequestNullRoom(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.approveDonationRequest(PENDING_DONATION_REQUEST_ID, null);
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
            donationRequest = service.approveDonationRequest(PENDING_DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(NONEXISTING_ID));
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
            donationRequest = service.approveDonationRequest(PENDING_DONATION_REQUEST_ID, roomRepository.findRoomByRoomId(SMALL_ROOM_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Selected room is not a storage room", error);
    }

    @Test
    public void testRejectDonationRequest(){
        DonationRequest donationRequest = null;
        try {
            donationRequest = service.rejectDonationRequest(PENDING_DONATION_REQUEST_ID);
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
    public void testRejectDonationRequestAndRequestAlreadyRejected(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.rejectDonationRequest(REJECTED_DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Donation request already rejected", error);
    }

    @Test
    public void testRejectDonationRequestAndRequestAlreadyApproved(){
        String error = null;

        DonationRequest donationRequest = null;
        try {
            donationRequest = service.rejectDonationRequest(APPROVED_DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(donationRequest);
        assertEquals("Donation request already approved", error);
    }

    @Test
    public void testGetExistingDonationRequest(){
        DonationRequest donationRequest = null;
        try {
            donationRequest = service.getDonationRequest(PENDING_DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(donationRequest);
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
    public void testDeletePendingDonationRequest(){

        String error = null;
        try {
            service.deleteRejectedDonationRequest(PENDING_DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Cannot delete a donation request that is pending", error);
    }

    @Test
    public void testDeleteApprovedDonationRequest(){
        String error = null;

        try {
            service.deleteRejectedDonationRequest(APPROVED_DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Cannot delete a donation request that has been approved", error);
    }

    @Test
    public void testDeleteRejectedDonationRequest(){
        try {
            service.deleteRejectedDonationRequest(REJECTED_DONATION_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertTrue(service.deleteRejectedDonationRequest(REJECTED_DONATION_REQUEST_ID));
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
    
    @Test
    public void testGetAllDonationRequests(){
        Iterable<DonationRequest> donationRequests = service.getAllDonationRequests();
        assertEquals(ALL_DONATION_REQUESTS, donationRequests);
    }

    @Test
    public void testGetAllDonationRequestsByStatus(){
        List<DonationRequest> donationRequests = service.getAllDonationRequestsByStatus(DonationRequest.DonationStatus.Approved);
        assertEquals(1, donationRequests.size());
        assertEquals(APPROVED_DONATION_REQUEST, donationRequests.get(0));
    }

    @Test
    public void testGetAllDonationRequestsByClient(){
        List<DonationRequest> donationRequests = service.getAllDonationRequestsByClient(CLIENT_A);
        assertEquals(1, donationRequests.size());
        assertEquals(PENDING_DONATION_REQUEST, donationRequests.get(0));
       
    }

    // Helper methods

    public static MuseumManagementSystem createMms(int id, String name, Time openTime, Time closeTime, int maxLoanNumber, double ticketFee) {
        MuseumManagementSystem mms = new MuseumManagementSystem();
        mms.setSystemId(id);
        mms.setName(name);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);
        return mms;
    }

    public static Client createClient(String username, String name, String password, int currentLoanNumber, MuseumManagementSystem mms) {
        Client client = new Client();
        client.setUsername(username);
        client.setName(name);
        client.setPassword(password);
        client.setCurrentLoanNumber(currentLoanNumber);
        client.setMuseumManagementSystem(mms);
        return client;
    }

    public static Artifact createArtifact(int id, String name, String image, String description, boolean isDamaged, double worth, MuseumManagementSystem mms) {
        Artifact artifact = new Artifact();
        artifact.setArtifactId(id);
        artifact.setName(name);
        artifact.setImage(image);
        artifact.setDescription(description);
        artifact.setIsDamaged(isDamaged);
        artifact.setWorth(worth);
        artifact.setMuseumManagementSystem(mms);
        return artifact;
    }

    public static DonationRequest createDonationRequest(int id, Client client, Artifact artifact, DonationRequest.DonationStatus status, MuseumManagementSystem mms) {
        DonationRequest donationRequest = new DonationRequest();
        donationRequest.setRequestId(id);
        donationRequest.setClient(client);
        donationRequest.setArtifact(artifact);
        donationRequest.setStatus(status);
        donationRequest.setMuseumManagementSystem(mms);
        return donationRequest;
    }

    public static Room createRoom(int id, String name, RoomType roomType, MuseumManagementSystem mms) {
        Room room = new Room();
        room.setRoomId(id);
        room.setName(name);
        room.setType(roomType);
        room.setMuseumManagementSystem(mms);
        return room;
    }
    
}
