package ca.mcgill.ecse321.MMSBackend.service;

import ca.mcgill.ecse321.MMSBackend.dao.ArtifactRepository;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestLoanRequestService {
    @Mock
    private LoanRequestRepository loanRequestDao;

    @InjectMocks
    private LoanRequestService loanRequestService;

    private static final int LOAN_REQUEST_KEY = 10;
    private static final int NONEXISTING_LOAN_REQUEST_KEY = 5;
    private static final int LOAN_DURATION = 5;
    private static final double LOAN_FEE = 100.00;
    private static final LoanRequest.LoanStatus LOAN_STATUS = LoanRequest.LoanStatus.Pending;
    private static final MuseumManagementSystem mms = new MuseumManagementSystem();
    private static final Artifact artifact = new Artifact();
    private static final Client client = new Client();

    @BeforeEach
    public void setMockOutput() {
        lenient().when(loanRequestDao.findLoanRequestByRequestId(anyInt())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(LOAN_REQUEST_KEY)) {
                LoanRequest loanRequest = new LoanRequest();
                loanRequest.setRequestId(LOAN_REQUEST_KEY);
                loanRequest.setLoanDuration(LOAN_DURATION);
                loanRequest.setFee(LOAN_FEE);
                loanRequest.setStatus(LOAN_STATUS);
                loanRequest.setMuseumManagementSystem(mms);
                loanRequest.setArtifact(artifact);
                loanRequest.setClient(client);
                return loanRequest;
            } else {
                return null;
            }
        });

        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(loanRequestDao.save(any(LoanRequest.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateLoanRequest(){
//        assertEquals(0, loanRequestService.getAllLoanRequests().size());

        int loanDuration = 13;
        double fee = 420.69;
        Artifact artifact = new Artifact();
        artifact.setLoanStatus(Artifact.LoanStatus.Available);
        Client client = new Client();
        MuseumManagementSystem mms = new MuseumManagementSystem();

        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(loanDuration, fee, artifact, client, mms);
        } catch (MuseumManagementSystemException e){
            fail();
        }

        assertNotNull(loanRequest);
        assertEquals(loanDuration, loanRequest.getLoanDuration());
        assertEquals(fee, loanRequest.getFee());
        assertEquals(artifact, loanRequest.getArtifact());
        assertEquals(LoanRequest.LoanStatus.Pending, loanRequest.getStatus());
        assertEquals(client, loanRequest.getClient());
        assertEquals(mms, loanRequest.getMuseumManagementSystem());
    }

    @Test
    public void testCreateLoanRequestNull() {
        int loanDuration = 0;
        double fee = 0.0;
        Artifact artifact = null;
        Client client = null;
        MuseumManagementSystem mms = null;
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(loanDuration, fee, artifact, client, mms);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        // check error
        assertEquals("Null values not allowed", error);
    }

    @Test
    public void testCreateLoanRequestWithUnavailableArtifact() {
        int loanDuration = 13;
        double fee = 420.69;
        Artifact artifact = new Artifact();
        artifact.setLoanStatus(Artifact.LoanStatus.Unavailable);
        Client client = new Client();
        MuseumManagementSystem mms = new MuseumManagementSystem();
        String error = null;
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.createLoanRequest(loanDuration, fee, artifact, client, mms);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(loanRequest);
        // check error
        assertEquals("Loan request cannot be created.", error);
    }

    @Test
    public void testGetExistingLoanRequest() {
        assertEquals(LOAN_REQUEST_KEY, loanRequestService.getLoanRequest(LOAN_REQUEST_KEY).getRequestId());
    }

    @Test
    public void testGetNonExistingLoanRequest() {
        String error = null;
        try {
            loanRequestService.getLoanRequest(NONEXISTING_LOAN_REQUEST_KEY);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertEquals("Loan request not found.", error);
    }

}
