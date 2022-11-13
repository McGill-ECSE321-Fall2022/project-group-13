package ca.mcgill.ecse321.MMSBackend.service;

import ca.mcgill.ecse321.MMSBackend.dao.ArtifactRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.LoanRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

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
    private static final String CLIENT_NAME = "Y/N";
    private static final String CLIENT_PASSWORD = "pas$word321";
    private static final int CLIENT_CURRENT_LOAN_NUMBER = 0;
    private static final String CLIENT_USERNAME_AT_MAX_LOAN = "RegisteredUserWithFiveLoans";
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

    @BeforeEach
    public void setMockOutput() {
        lenient().when(mmsRepository.findMuseumManagementSystemBySystemId(anyInt()))
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
        lenient().when(artifactRepository.findArtifactByArtifactId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(AVAILABLE_ARTIFACT_ID)) {
                        Artifact artifact = new Artifact();
                        artifact.setArtifactId(AVAILABLE_ARTIFACT_ID);
                        artifact.setName(ARTIFACT_NAME);
                        artifact.setImage(ARTIFACT_IMAGE);
                        artifact.setDescription(ARTIFACT_DESCRIPTION);
                        artifact.setIsDamaged(ARTIFACT_IS_DAMAGED);
                        artifact.setWorth(ARTIFACT_WORTH);
                        artifact.setLoanFee(LOAN_FEE);
                        artifact.setLoanStatus(ARTIFACT_LOAN_STATUS_AVAILABLE);
                        artifact.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        return artifact;
                    } else if (invocation.getArgument(0).equals(UNAVAILABLE_ARTIFACT_ID)) {
                        Artifact artifact = new Artifact();
                        artifact.setArtifactId(UNAVAILABLE_ARTIFACT_ID);
                        artifact.setName(ARTIFACT_NAME);
                        artifact.setImage(ARTIFACT_IMAGE);
                        artifact.setDescription(ARTIFACT_DESCRIPTION);
                        artifact.setIsDamaged(ARTIFACT_IS_DAMAGED);
                        artifact.setWorth(ARTIFACT_WORTH);
                        artifact.setLoanFee(LOAN_FEE);
                        artifact.setLoanStatus(ARTIFACT_LOAN_STATUS_UNAVAILABLE);
                        artifact.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        return artifact;
                    } else if (invocation.getArgument(0).equals(LOANED_ARTIFACT_ID)) {
                        Artifact artifact = new Artifact();
                        artifact.setArtifactId(LOANED_ARTIFACT_ID);
                        artifact.setName(ARTIFACT_NAME);
                        artifact.setImage(ARTIFACT_IMAGE);
                        artifact.setDescription(ARTIFACT_DESCRIPTION);
                        artifact.setIsDamaged(ARTIFACT_IS_DAMAGED);
                        artifact.setWorth(ARTIFACT_WORTH);
                        artifact.setLoanFee(LOAN_FEE);
                        artifact.setLoanStatus(ARTIFACT_LOAN_STATUS_LOANED);
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
                    } else if (invocation.getArgument(0).equals(CLIENT_USERNAME_AT_MAX_LOAN)) {
                        Client client = new Client();
                        client.setUsername(CLIENT_USERNAME);
                        client.setName(CLIENT_NAME);
                        client.setPassword(CLIENT_PASSWORD);
                        client.setCurrentLoanNumber(CLIENT_AT_MAX_LOAN_NUMBER);
                        client.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        return client;
                    } else {
                        return null;
                    }
                });
        lenient().when(loanRequestDao.findLoanRequestByRequestId(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(LOAN_REQUEST_ID)) {
                        LoanRequest loanRequest = new LoanRequest();
                        loanRequest.setRequestId(LOAN_REQUEST_ID);
                        loanRequest.setLoanDuration(LOAN_DURATION);
                        loanRequest.setStatus(LoanRequest.LoanStatus.Pending);
                        loanRequest.setFee(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID).getLoanFee());
                        loanRequest.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        loanRequest.setArtifact(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID));
                        loanRequest.setClient(clientRepository.findClientByUsername(CLIENT_USERNAME_AT_MAX_LOAN));
                        return loanRequest;
                    } else if (invocation.getArgument(0).equals(PENDING_LOAN_REQUEST_ID)) {
                        LoanRequest loanRequest = new LoanRequest();
                        loanRequest.setRequestId(PENDING_LOAN_REQUEST_ID);
                        loanRequest.setLoanDuration(LOAN_DURATION);
                        loanRequest.setStatus(LoanRequest.LoanStatus.Pending);
                        loanRequest.setFee(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID).getLoanFee());
                        loanRequest.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        loanRequest.setArtifact(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID));
                        loanRequest.setClient(clientRepository.findClientByUsername(CLIENT_USERNAME));
                        return loanRequest;
                    } else if (invocation.getArgument(0).equals(APPROVED_LOAN_REQUEST_ID)) {
                        LoanRequest loanRequest = new LoanRequest();
                        loanRequest.setRequestId(APPROVED_LOAN_REQUEST_ID);
                        loanRequest.setLoanDuration(LOAN_DURATION);
                        loanRequest.setStatus(LoanRequest.LoanStatus.Approved);
                        loanRequest.setFee(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID).getLoanFee());
                        loanRequest.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        loanRequest.setArtifact(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID));
                        loanRequest.setClient(clientRepository.findClientByUsername(CLIENT_USERNAME));
                        return loanRequest;
                    } else if (invocation.getArgument(0).equals(REJECTED_LOAN_REQUEST_ID)) {
                        LoanRequest loanRequest = new LoanRequest();
                        loanRequest.setRequestId(REJECTED_LOAN_REQUEST_ID);
                        loanRequest.setLoanDuration(LOAN_DURATION);
                        loanRequest.setStatus(LoanRequest.LoanStatus.Rejected);
                        loanRequest.setFee(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID).getLoanFee());
                        loanRequest.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        loanRequest.setArtifact(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID));
                        loanRequest.setClient(clientRepository.findClientByUsername(CLIENT_USERNAME));
                        return loanRequest;
                    } else if (invocation.getArgument(0).equals(RETURNED_LOAN_REQUEST_ID)) {
                        LoanRequest loanRequest = new LoanRequest();
                        loanRequest.setRequestId(RETURNED_LOAN_REQUEST_ID);
                        loanRequest.setLoanDuration(LOAN_DURATION);
                        loanRequest.setStatus(LoanRequest.LoanStatus.Returned);
                        loanRequest.setFee(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID).getLoanFee());
                        loanRequest.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(MMS_ID));
                        loanRequest.setArtifact(artifactRepository.findArtifactByArtifactId(AVAILABLE_ARTIFACT_ID));
                        loanRequest.setClient(clientRepository.findClientByUsername(CLIENT_USERNAME));
                        return loanRequest;
                    } else {
                        return null;
                    }
                });

        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(loanRequestDao.save(any(LoanRequest.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(artifactRepository.save(any(Artifact.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(clientRepository.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(mmsRepository.save(any(MuseumManagementSystem.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateLoanRequest() {
        assertEquals(0, loanRequestService.getAllLoanRequests().size());
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
        checkCreatedLoanRequest(loanRequest);
    }

    @Test
    public void testCreateLoanRequestNullLoanDuration() {
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
        assertEquals("Loan duration must be indicated", error);
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
        checkCreatedLoanRequest(loanRequest);
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
    public void testApproveLoanRequestAndRequestAlreadyRejected() {
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
        checkCreatedLoanRequest(loanRequest);
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
        checkCreatedLoanRequest(loanRequest);
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
        assertEquals("Loan needs to be approved or rejected first.", error);
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

    private void checkCreatedLoanRequest(LoanRequest loanRequest) {
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

}
