package ca.mcgill.ecse321.MMSBackend.service;

import ca.mcgill.ecse321.MMSBackend.dao.ArtifactRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

/**
 * @author Nazia Chowdhury (naziaC)
 * The TestLoanRequestService class is responsible for testing the
 * business logic declared in LoanRequestService.
 */
@ExtendWith(MockitoExtension.class)
public class TestLoanRequestService {

    @Mock
    private LoanRequestRepository loanRequestDao;
    @Mock
    private ArtifactRepository artifactRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private MuseumManagementSystemRepository mmsRepository;

    @InjectMocks
    private LoanRequestService loanRequestService;

    private static final int LOAN_REQUEST_ID = 1;
    private static final int PENDING_LOAN_REQUEST_ID = 2;
    private static final int APPROVED_LOAN_REQUEST_ID = 3;
    private static final int REJECTED_LOAN_REQUEST_ID = 4;
    private static final int RETURNED_LOAN_REQUEST_ID = 5;

    private static final int AVAILABLE_ARTIFACT_ID = 6;
    private static final int UNAVAILABLE_ARTIFACT_ID = 7;
    private static final int LOANED_ARTIFACT_ID = 8;
    private static final String ARTIFACT_NAME = "An Artifact";
    private static final String ARTIFACT_IMAGE = "artifact.jpg";
    private static final String ARTIFACT_DESCRIPTION = "Artifact Description";
    private static final boolean ARTIFACT_IS_DAMAGED = false;
    private static final double ARTIFACT_WORTH = 10.0;

    private static final String CLIENT_USERNAME = "RegisteredUser";
    private static final String CLIENT_USERNAME_AT_MAX_LOAN = "RegisteredUserWithFiveLoans";
    private static final String CLIENT_NAME = "Y/N";
    private static final String CLIENT_PASSWORD = "pas$word321";
    private static final int CLIENT_CURRENT_LOAN_NUMBER = 2;
    private static final int CLIENT_AT_MAX_LOAN_NUMBER = 5;

    private static final int MMS_ID = 13;
    private static final String MMS_NAME = "Marwan's Museum";
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final int MAX_LOAN_NUMBER = 5;
    private static final double TICKET_FEE = 42.0;

    private static final int NONEXISTING_ID = 0;
    private static final String NONEXISTING_USERNAME = "Unknown User";

    private static final int LOAN_DURATION = 13;
    private static final double LOAN_FEE = 100.00;
    private static final Artifact.LoanStatus ARTIFACT_LOAN_STATUS_AVAILABLE = Artifact.LoanStatus.Available;
    private static final Artifact.LoanStatus ARTIFACT_LOAN_STATUS_UNAVAILABLE = Artifact.LoanStatus.Unavailable;
    private static final Artifact.LoanStatus ARTIFACT_LOAN_STATUS_LOANED = Artifact.LoanStatus.Loaned;
    private static final MuseumManagementSystem museumManagementSystem = createMms();
    private static final Client client = clientCreationHelper(CLIENT_USERNAME, CLIENT_CURRENT_LOAN_NUMBER);
    private static final Artifact artifact = artifactCreationHelper(AVAILABLE_ARTIFACT_ID, ARTIFACT_LOAN_STATUS_AVAILABLE);
    private static final Iterable<LoanRequest> ALL_LOAN_REQUESTS = Arrays.asList(
            loanRequestCreationHelper(9, artifact, client, LoanRequest.LoanStatus.Pending),
            loanRequestCreationHelper(10, artifact, client, LoanRequest.LoanStatus.Approved)
    );

    @BeforeEach
    public void setMockOutput() {
        lenient().when(mmsRepository.findMuseumManagementSystemBySystemId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(MMS_ID)) {
                        return createMms();
                    } else {
                        return null;
                    }
                });
        lenient().when(artifactRepository.findArtifactByArtifactId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(AVAILABLE_ARTIFACT_ID)) {
                        return artifactCreationHelper(AVAILABLE_ARTIFACT_ID, ARTIFACT_LOAN_STATUS_AVAILABLE);
                    } else if (invocation.getArgument(0).equals(UNAVAILABLE_ARTIFACT_ID)) {
                        return artifactCreationHelper(UNAVAILABLE_ARTIFACT_ID, ARTIFACT_LOAN_STATUS_UNAVAILABLE);
                    } else if (invocation.getArgument(0).equals(LOANED_ARTIFACT_ID)) {
                        return artifactCreationHelper(LOANED_ARTIFACT_ID, ARTIFACT_LOAN_STATUS_LOANED);
                    } else {
                        return null;
                    }
                });
        lenient().when(clientRepository.findClientByUsername(anyString()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(CLIENT_USERNAME)) {
                        return clientCreationHelper(CLIENT_USERNAME, CLIENT_CURRENT_LOAN_NUMBER);
                    } else if (invocation.getArgument(0).equals(CLIENT_USERNAME_AT_MAX_LOAN)) {
                        return clientCreationHelper(CLIENT_USERNAME_AT_MAX_LOAN, CLIENT_AT_MAX_LOAN_NUMBER);
                    } else {
                        return null;
                    }
                });
        lenient().when(loanRequestDao.findLoanRequestByRequestId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(LOAN_REQUEST_ID)) {
                        return loanRequestCreationHelper(LOAN_REQUEST_ID, artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                                clientRepository.findClientByUsername(CLIENT_USERNAME_AT_MAX_LOAN), LoanRequest.LoanStatus.Pending);
                    } else if (invocation.getArgument(0).equals(PENDING_LOAN_REQUEST_ID)) {
                        return loanRequestCreationHelper(PENDING_LOAN_REQUEST_ID, artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                                clientRepository.findClientByUsername(CLIENT_USERNAME), LoanRequest.LoanStatus.Pending);
                    } else if (invocation.getArgument(0).equals(APPROVED_LOAN_REQUEST_ID)) {
                        return loanRequestCreationHelper(APPROVED_LOAN_REQUEST_ID, artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                                clientRepository.findClientByUsername(CLIENT_USERNAME), LoanRequest.LoanStatus.Approved);
                    } else if (invocation.getArgument(0).equals(REJECTED_LOAN_REQUEST_ID)) {
                        return loanRequestCreationHelper(REJECTED_LOAN_REQUEST_ID, artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                                clientRepository.findClientByUsername(CLIENT_USERNAME), LoanRequest.LoanStatus.Rejected);
                    } else if (invocation.getArgument(0).equals(RETURNED_LOAN_REQUEST_ID)) {
                        return loanRequestCreationHelper(RETURNED_LOAN_REQUEST_ID, artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                                clientRepository.findClientByUsername(CLIENT_USERNAME),LoanRequest.LoanStatus.Returned);
                    } else {
                        return null;
                    }
                });
        lenient().when(loanRequestDao.findAll()).thenReturn(ALL_LOAN_REQUESTS);

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(loanRequestDao.save(any(LoanRequest.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(artifactRepository.save(any(Artifact.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(clientRepository.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(mmsRepository.save(any(MuseumManagementSystem.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateLoanRequest() {
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(LOAN_DURATION,
                    artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                    clientRepository.findClientByUsername(CLIENT_USERNAME),
                    mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(loanRequest);
        assertEquals(LoanRequest.LoanStatus.Pending, loanRequest.getStatus());
        checkLoanRequestInformation(loanRequest);
    }

    @Test
    public void testCreateLoanRequestInvalidLoanDuration() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(0,
                    artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                    clientRepository.findClientByUsername(CLIENT_USERNAME),
                    mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Loan duration must be valid", error);
    }

    @Test
    public void testCreateLoanRequestNullArtifact() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(LOAN_DURATION,
                    null,
                    clientRepository.findClientByUsername(CLIENT_USERNAME),
                    mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Artifact does not exist", error);
    }

    @Test
    public void testCreateLoanRequestNullClient() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(LOAN_DURATION,
                    artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                    null,
                    mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Client does not exist", error);
    }

    @Test
    public void testCreateLoanRequestNullMMS() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(LOAN_DURATION,
                    artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                    clientRepository.findClientByUsername(CLIENT_USERNAME),
                    null);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Museum Management System does not exist", error);
    }

    @Test
    public void testCreateLoanRequestWithUnavailableArtifact() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(LOAN_DURATION,
                    artifactRepository.findArtifactByArtifactId(UNAVAILABLE_ARTIFACT_ID),
                    clientRepository.findClientByUsername(CLIENT_USERNAME),
                    mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Loan request cannot be created.", error);
    }

    @Test
    public void testCreateLoanRequestAndArtifactDoesNotExist() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(LOAN_DURATION,
                    artifactRepository.findArtifactByArtifactId(NONEXISTING_ID),
                    clientRepository.findClientByUsername(CLIENT_USERNAME),
                    mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Artifact does not exist", error);
    }

    @Test
    public void testCreateLoanRequestAndClientDoesNotExist() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(LOAN_DURATION,
                    artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                    clientRepository.findClientByUsername(NONEXISTING_USERNAME),
                    mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Client does not exist", error);
    }

    @Test
    public void testCreateLoanRequestAndMMSDoesNotExist() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(LOAN_DURATION,
                    artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID),
                    clientRepository.findClientByUsername(CLIENT_USERNAME),
                    mmsRepository.findMuseumManagementSystemBySystemId(NONEXISTING_ID));
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Museum Management System does not exist", error);
    }

    @Test
    public void testApproveLoanRequest() {
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.approveLoanRequest(PENDING_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }
        assertNotNull(loanRequest);
        assertEquals(LoanRequest.LoanStatus.Approved, loanRequest.getStatus());
        assertEquals(Artifact.LoanStatus.Loaned, loanRequest.getArtifact().getLoanStatus());
        assertEquals(CLIENT_CURRENT_LOAN_NUMBER + 1, loanRequest.getClient().getCurrentLoanNumber());
        checkLoanRequestInformation(loanRequest);
    }

    @Test
    public void testApproveLoanRequestAndLoanRequestDoesNotExist() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.approveLoanRequest(NONEXISTING_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Loan request not found.", error);
    }

    @Test
    public void testApproveLoanRequestAndRequestAlreadyApproved() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.approveLoanRequest(APPROVED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Loan request was already approved.", error);
    }

    @Test
    public void testApproveLoanRequestAndRequestIsAlreadyRejected() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.approveLoanRequest(REJECTED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Loan request was already rejected.", error);
    }

    @Test
    public void testApproveLoanRequestAndLoanWasAlreadyReturned() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.approveLoanRequest(RETURNED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Loan was already returned.", error);
    }

    @Test
    public void testApproveLoanRequestAndClientHasMaxLoanNumberAlready() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.approveLoanRequest(LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Client has reached maximum number of loans.", error);
    }

    @Test
    public void testRejectLoanRequest() {
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.rejectLoanRequest(PENDING_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(loanRequest);
        assertEquals(LoanRequest.LoanStatus.Rejected, loanRequest.getStatus());
        checkLoanRequestInformation(loanRequest);
    }

    @Test
    public void testRejectLoanRequestAndLoanRequestDoesNotExist() {
        String error = null;

        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.rejectLoanRequest(NONEXISTING_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Loan request not found.", error);
    }

    @Test
    public void testRejectLoanRequestAndRequestAlreadyApproved() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.rejectLoanRequest(APPROVED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Loan request was already approved.", error);
    }

    @Test
    public void testRejectLoanRequestAndRequestAlreadyRejected() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.rejectLoanRequest(REJECTED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Loan request was already rejected.", error);
    }

    @Test
    public void testRejectLoanRequestAndLoanWasAlreadyReturned() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.rejectLoanRequest(RETURNED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Loan was already returned.", error);
    }

    @Test
    public void testReturnLoanedArtifact() {
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.returnLoanedArtifact(APPROVED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }
        assertNotNull(loanRequest);
        assertEquals(LoanRequest.LoanStatus.Returned, loanRequest.getStatus());
        assertEquals(Artifact.LoanStatus.Available, loanRequest.getArtifact().getLoanStatus());
        assertEquals(CLIENT_CURRENT_LOAN_NUMBER - 1, loanRequest.getClient().getCurrentLoanNumber());
        checkLoanRequestInformation(loanRequest);
    }

    @Test
    public void testReturnLoanedArtifactAndLoanRequestDoesNotExist() {
        String error = null;

        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.returnLoanedArtifact(NONEXISTING_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Loan request not found.", error);
    }

    @Test
    public void testReturnLoanedArtifactAndRequestIsPending() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.returnLoanedArtifact(PENDING_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Artifact was not loaned.", error);
    }

    @Test
    public void testReturnLoanedArtifactAndRequestAlreadyRejected() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.returnLoanedArtifact(REJECTED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Loan request was rejected.", error);
    }

    @Test
    public void testReturnLoanedArtifactAndLoanWasAlreadyReturned() {
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.returnLoanedArtifact(RETURNED_LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        assertEquals("Loan was already returned.", error);
    }

    @Test
    public void testGetExistingLoanRequest(){
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.getLoanRequest(LOAN_REQUEST_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }
        assertNotNull(loanRequest);
    }

    @Test
    public void testGetNonExistingLoanRequest() {
        String error = null;

        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.getLoanRequest(NONEXISTING_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(loanRequest);
        assertEquals("Loan request not found.", error);
    }

    @Test
    public void testGetAllLoanRequests() {
        Iterable<LoanRequest> loanRequests = loanRequestService.getAllLoanRequests();
        assertEquals(ALL_LOAN_REQUESTS, loanRequests);
    }

     @Test
     public void testGetAllLoanRequestsByStatus(){
         List<LoanRequest> loanRequests = loanRequestService.getAllLoanRequestsByStatus(LoanRequest.LoanStatus.Approved);
         assertNotNull(loanRequests);
         assertEquals(1, loanRequests.size());
     }

    @Test
    public void testGetAllLoanRequestsByStatusWhenThereIsNoLoanRequestsOfThatStatus(){
        List<LoanRequest> loanRequests = loanRequestService.getAllLoanRequestsByStatus(LoanRequest.LoanStatus.Returned);
        assertNotNull(loanRequests);
        assertEquals(0, loanRequests.size());
    }

    @Test
    public void testGetAllActiveLoanRequests(){
        List<LoanRequest> loanRequests = loanRequestService.getAllActiveLoanRequests();
        assertNotNull(loanRequests);
        assertEquals(1, loanRequests.size());
    }

    @Test
    public void testGetAllLoanRequestsByClient(){
        List<LoanRequest> loanRequests = loanRequestService.getAllLoanRequestsByClient(client);
        assertNotNull(loanRequests);
        assertEquals(2, loanRequests.size());
    }

    @Test
    public void testGetAllLoanRequestsByClientWhenThereIsNoLoanRequestsOfThatClient(){
        List<LoanRequest> loanRequests = loanRequestService.getAllLoanRequestsByClient(clientCreationHelper(CLIENT_USERNAME_AT_MAX_LOAN, CLIENT_AT_MAX_LOAN_NUMBER));
        assertNotNull(loanRequests);
        assertEquals(0, loanRequests.size());
    }

    /**
     * Checks the information in the loan request
     * @param loanRequest
     */
    private void checkLoanRequestInformation(LoanRequest loanRequest) {
        assertEquals(LOAN_DURATION, loanRequest.getLoanDuration());
        assertEquals(LOAN_FEE, loanRequest.getFee());

        assertEquals(CLIENT_NAME, loanRequest.getClient().getName());
        assertEquals(CLIENT_PASSWORD, loanRequest.getClient().getPassword());

        assertEquals(ARTIFACT_NAME, loanRequest.getArtifact().getName());
        assertEquals(ARTIFACT_IMAGE, loanRequest.getArtifact().getImage());
        assertEquals(ARTIFACT_DESCRIPTION, loanRequest.getArtifact().getDescription());
        assertEquals(ARTIFACT_IS_DAMAGED, loanRequest.getArtifact().getIsDamaged());
        assertEquals(ARTIFACT_WORTH, loanRequest.getArtifact().getWorth());

        assertEquals(MMS_NAME, loanRequest.getMuseumManagementSystem().getName());
        assertEquals(OPEN_TIME, loanRequest.getMuseumManagementSystem().getOpenTime());
        assertEquals(CLOSE_TIME, loanRequest.getMuseumManagementSystem().getCloseTime());
        assertEquals(MAX_LOAN_NUMBER, loanRequest.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(TICKET_FEE, loanRequest.getMuseumManagementSystem().getTicketFee());
    }

    /**
     * Private Constructor of an artifact
     * @param artifactId
     * @param artifactStatus
     */
    private static Artifact artifactCreationHelper(int artifactId, Artifact.LoanStatus artifactStatus){
        Artifact artifact = new Artifact();
        artifact.setArtifactId(artifactId);
        artifact.setName(ARTIFACT_NAME);
        artifact.setImage(ARTIFACT_IMAGE);
        artifact.setDescription(ARTIFACT_DESCRIPTION);
        artifact.setIsDamaged(ARTIFACT_IS_DAMAGED);
        artifact.setWorth(ARTIFACT_WORTH);
        artifact.setLoanFee(LOAN_FEE);
        artifact.setLoanStatus(artifactStatus);
        artifact.setMuseumManagementSystem(museumManagementSystem);
        return artifact;
    }

    /**
     * Private Constructor of a client
     * @param username
     * @param loanNumber
     */
    private static Client clientCreationHelper(String username, int loanNumber){
        Client client = new Client();
        client.setUsername(username);
        client.setName(CLIENT_NAME);
        client.setPassword(CLIENT_PASSWORD);
        client.setCurrentLoanNumber(loanNumber);
        client.setMuseumManagementSystem(museumManagementSystem);
        return client;
    }

    /**
     * Private Constructor of a loan request
     * @param requestId
     * @param loanRequestStatus
     */
    private static LoanRequest loanRequestCreationHelper(int requestId, Artifact artifact, Client client, LoanRequest.LoanStatus loanRequestStatus){
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setRequestId(requestId);
        loanRequest.setLoanDuration(LOAN_DURATION);
        loanRequest.setStatus(loanRequestStatus);
        loanRequest.setFee(artifact.getLoanFee());
        loanRequest.setMuseumManagementSystem(museumManagementSystem);
        loanRequest.setArtifact(artifact);
        loanRequest.setClient(client);
        return loanRequest;
    }

    /**
     * Private Constructor of a MuseumManagementSystem
     */
    private static MuseumManagementSystem createMms() {
        MuseumManagementSystem mms = new MuseumManagementSystem();
        mms.setSystemId(MMS_ID);
        mms.setName(MMS_NAME);
        mms.setOpenTime(OPEN_TIME);
        mms.setCloseTime(CLOSE_TIME);
        mms.setMaxLoanNumber(MAX_LOAN_NUMBER);
        mms.setTicketFee(TICKET_FEE);
        return mms;
    }
}
