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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nazia Chowdhury (naziaC)
 * The LoanRequestService class implements the use case based on the requirement:
 * <p>
 * “FREQ08: Upon the request of a loan, the museum management system shall generate a loan request,
 * which includes the client’s username, artifact’s id, period of loan, and the status of the loan request,
 * which has a status (pending, approved, rejected, or returned) that can be updated by the museum staff.”
 */
@Service
public class LoanRequestService {

    @Autowired
    LoanRequestRepository loanRequestRepository;
    @Autowired
    ArtifactRepository artifactRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    MuseumManagementSystemRepository mmsRepository;

    /**
     * Creates a loan request pending for approval
     *
     * @param loanDuration
     * @param artifact
     * @param client
     * @param mms
     */
    @Transactional
    public LoanRequest createLoanRequest(int loanDuration, Artifact artifact, Client client, MuseumManagementSystem mms) {
        if (client == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Client does not exist");
        else if (artifact == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Artifact does not exist");
        else if (mms == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Museum Management System does not exist");
        else if (loanDuration == 0)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan duration must be indicated");

        LoanRequest loanRequest;
        if (artifact.getLoanStatus().equals(Artifact.LoanStatus.Available)) {
            loanRequest = new LoanRequest();
            loanRequest.setArtifact(artifact);
            loanRequest.setClient(client);
            loanRequest.setLoanDuration(loanDuration);
            loanRequest.setFee(artifact.getLoanFee());
            loanRequest.setStatus(LoanRequest.LoanStatus.Pending);
            loanRequest.setMuseumManagementSystem(mms);
            loanRequestRepository.save(loanRequest);
        } else {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan request cannot be created.");
        }
        return loanRequest;
    }

    /**
     * Gets a loan request by its id
     *
     * @param requestId
     * @return loan request object
     */
    @Transactional
    public LoanRequest getLoanRequest(Integer requestId) {
        if (loanRequestRepository.findLoanRequestByRequestId(requestId) == null)
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Loan request not found.");

        return loanRequestRepository.findLoanRequestByRequestId(requestId);

//        LoanRequest loanRequest  = loanRequestRepository.findLoanRequestByRequestId(requestId);
//        if (loanRequest == null) {
//            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Loan request not found.");
//        }
//        return loanRequest;
    }

    /**
     * Gets all loan requests registered
     *
     * @return a list of loan request objects
     */
    @Transactional
    public List<LoanRequest> getAllLoanRequests() {
        return toList(loanRequestRepository.findAll());
    }

    /**
     * Gets all loan requests registered in the specified museum by its status
     *
     * @param status
     * @return a list of loan request objects that have a specific status
     */
    @Transactional
    public List<LoanRequest> getAllLoanRequestsByStatus(LoanRequest.LoanStatus status) {
        List<LoanRequest> loanRequestsByStatus = new ArrayList<LoanRequest>();
        for (LoanRequest loanRequest : loanRequestRepository.findAll()) {
            if (loanRequest.getStatus().equals(status)) {
                loanRequestsByStatus.add(loanRequest);
            }
        }
        return loanRequestsByStatus;
    }

    /**
     * Gets all loan requests than can be approved/rejected
     *
     * @return a list of loan request objects for a specific client than can be approved/rejected
     */
    @Transactional
    public List<LoanRequest> getAllActiveLoanRequests() {
        List<LoanRequest> loanRequestsByStatus = new ArrayList<LoanRequest>();
        for (LoanRequest loanRequest : loanRequestRepository.findAll()) {
            if (loanRequest.getStatus().equals(LoanRequest.LoanStatus.Pending) && loanRequest.getClient().getCurrentLoanNumber() < 5) {
                loanRequestsByStatus.add(loanRequest);
            }
        }
        return loanRequestsByStatus;
    }

    /**
     * Gets all loan requests registered by its client
     *
     * @param client
     * @return a list of loan request objects
     */
    @Transactional
    public List<LoanRequest> getAllLoanRequestsByClient(Client client) {
        List<LoanRequest> loanRequestsByClient = new ArrayList<LoanRequest>();
        for (LoanRequest loanRequest : loanRequestRepository.findAll()) {
            if (loanRequest.getClient().equals(client)) {
                loanRequestsByClient.add(loanRequest);
            }
        }
        return loanRequestsByClient;
    }

    /**
     * Approve loan request
     *
     * @param requestId
     * @return loan request object
     */
    @Transactional
    public LoanRequest approveLoanRequest(Integer requestId) {
        // Checking if loan request exists
        LoanRequest loanRequest;
        if (loanRequestRepository.findLoanRequestByRequestId(requestId) == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Loan request not found.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Approved)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan request was already approved.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Rejected)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan request was already rejected.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Returned)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan was already returned.");
        } else {
            // Approving loan request
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            Artifact artifact = loanRequest.getArtifact();
            Client client = loanRequest.getClient();

            if (client.getCurrentLoanNumber() < 5) {
                loanRequest.setStatus(LoanRequest.LoanStatus.Approved);
                artifact.setLoanStatus(Artifact.LoanStatus.Loaned);
                client.setCurrentLoanNumber(client.getCurrentLoanNumber() + 1);

                loanRequestRepository.save(loanRequest);
                artifactRepository.save(artifact);
                clientRepository.save(client);
            } else {
                throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Client has reached maximum number of loans.");
            }
        }
        return loanRequest;
    }

    /**
     * Reject loan request
     *
     * @param requestId
     * @return loan request object
     */
    @Transactional
    public LoanRequest rejectLoanRequest(Integer requestId) {
        // Checking if loan request exists
        LoanRequest loanRequest;
        if (loanRequestRepository.findLoanRequestByRequestId(requestId) == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Loan request not found.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Approved)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan request was already approved.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Rejected)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan request was already rejected.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Returned)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan was already returned.");
        } else {
            // Rejecting loan request
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            loanRequest.setStatus(LoanRequest.LoanStatus.Rejected);
            loanRequestRepository.save(loanRequest);
        }
        return loanRequest;
    }

    /**
     * Confirm artifact's return
     *
     * @param requestId
     * @return loan request object
     */
    @Transactional
    public LoanRequest returnLoanedArtifact(Integer requestId) {
        // Checking if loan request exists
        LoanRequest loanRequest;
        if (loanRequestRepository.findLoanRequestByRequestId(requestId) == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Loan request not found.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Rejected)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan request was rejected.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Returned)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan was already returned.");
        } else if (loanRequestRepository.findLoanRequestByRequestId(requestId).getStatus().equals(LoanRequest.LoanStatus.Pending)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Loan needs to be approved or rejected first.");
        } else {
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            Artifact artifact = loanRequest.getArtifact();
            Client client = loanRequest.getClient();

            loanRequest.setStatus(LoanRequest.LoanStatus.Returned);
            artifact.setLoanStatus(Artifact.LoanStatus.Available);
            client.setCurrentLoanNumber(client.getCurrentLoanNumber() - 1);

            loanRequestRepository.save(loanRequest);
            artifactRepository.save(artifact);
            clientRepository.save(client);
        }
        return loanRequest;
    }

    /**
     * toList helper method (@author eventRegistration authors)
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}
