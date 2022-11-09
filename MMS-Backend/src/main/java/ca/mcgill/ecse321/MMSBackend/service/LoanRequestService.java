package ca.mcgill.ecse321.MMSBackend.service;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nazia Chowdhury (naziaC)
 * The LoanRequestService class implements the use case based on the requirement:
 *
 * “FREQ08: Upon the request of a loan, the museum management system shall generate a loan request,
 * which includes the client’s username, artifact’s id, period of loan, and the status of the loan request,
 * which has a status (pending, approved, rejected, or returned) that can be updated by the museum staff.”
 *
 */
@Service
public class LoanRequestService {

    @Autowired
    LoanRequestRepository loanRequestRepository;
    @Autowired
    ArtifactRepository artifactRepository;

    /**
     * Creates a loan request pending for approval
     * @param loanDuration
     * @param fee
     * @param artifact
     * @param client
     * @param mms
     */
    @Transactional
    public LoanRequest createLoanRequest(int loanDuration, double fee, Artifact artifact, Client client, MuseumManagementSystem mms){
        LoanRequest loanRequest;
        if (artifact.getLoanStatus().equals(Artifact.LoanStatus.Available)){
            loanRequest = new LoanRequest();
            loanRequest.setArtifact(artifact);
            loanRequest.setClient(client);
            loanRequest.setLoanDuration(loanDuration);
            loanRequest.setFee(fee);
            loanRequest.setStatus(LoanRequest.LoanStatus.Pending);
            loanRequest.setMuseumManagementSystem(mms);
            loanRequestRepository.save(loanRequest);
        } else {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Artifact cannot be loaned.");
        }
        return loanRequest;
    }

    /**
     * Gets a loan request by its id
     * @param requestId
     * @return loan request object
     */
    @Transactional
    public LoanRequest getLoanRequest(Integer requestId){
        LoanRequest loanRequest  = loanRequestRepository.findLoanRequestByRequestId(requestId);
        if (loanRequest == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Loan request not found.");
        }
        return loanRequest;
    }

    /**
     * Gets all loan requests registered in the specific museum
     * @param systemId
     * @return a list of loan request objects
     */
    @Transactional
    public List<LoanRequest> getAllLoanRequestsBySystem(int systemId) {
        List<LoanRequest> loanRequests = new ArrayList<LoanRequest>();
        for (LoanRequest loanRequest : loanRequestRepository.findAll()) {
            if (loanRequest.getMuseumManagementSystem().getSystemId() == systemId) {
                loanRequests.add(loanRequest);
            }
        }
        return loanRequests;
    }

    /**
     * Gets all loan requests registered in the specified museum by its status
     * @param systemId
     * @param status
     * @return a list of loan request objects that have a specific status
     */
    @Transactional
    public List<LoanRequest> getAllLoanRequestsByStatus(LoanRequest.LoanStatus status, int systemId) {
        List<LoanRequest> loanRequestsByStatus = new ArrayList<LoanRequest>();
        for (LoanRequest loanRequest : loanRequestRepository.findAll()) {
            if (loanRequest.getStatus().equals(status) && loanRequest.getMuseumManagementSystem().getSystemId() == systemId) {
                loanRequestsByStatus.add(loanRequest);
            }
        }
        return loanRequestsByStatus;
    }

    /**
     * Gets all loan requests registered by its client
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
     * @param requestId
     * @return loan request object
     */
    @Transactional
    public LoanRequest approveLoanRequest(Integer requestId){
        // Checking if loan request exists
        LoanRequest loanRequest=null;
        if (loanRequestRepository.existsById(requestId)){
            // Approving loan request
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            Artifact artifact = loanRequest.getArtifact();

            artifact.setLoanStatus(Artifact.LoanStatus.Unavailable);
            loanRequest.setStatus(LoanRequest.LoanStatus.Approved);

            artifactRepository.save(artifact);
            loanRequestRepository.save(loanRequest);
        }
        return loanRequest;
    }

    /**
     * Reject loan request
     * @param requestId
     * @return loan request object
     */
    @Transactional
    public LoanRequest rejectLoanRequest(Integer requestId){
        // Checking if loan request exists
        LoanRequest loanRequest = null;
        if (loanRequestRepository.existsById(requestId)){
            // Rejecting loan request
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            loanRequest.setStatus(LoanRequest.LoanStatus.Rejected);
            loanRequestRepository.save(loanRequest);
        }
        return loanRequest;
    }

    /**
     * Confirm artifact's return
     * @param requestId
     * @return loan request object
     */
    @Transactional
    public LoanRequest returnLoanArtifact(Integer requestId){
        // Checking if loan request exists
        LoanRequest loanRequest=null;
        if (loanRequestRepository.existsById(requestId)){
            // Returned artifact
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            Artifact artifact = loanRequest.getArtifact();

            artifact.setLoanStatus(Artifact.LoanStatus.Available);
            loanRequest.setStatus(LoanRequest.LoanStatus.Returned);

            artifactRepository.save(artifact);
            loanRequestRepository.save(loanRequest);
        }
        return loanRequest;
    }

    /**
     * toList helper method (@author eventRegistration authors)
     */
    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

}
