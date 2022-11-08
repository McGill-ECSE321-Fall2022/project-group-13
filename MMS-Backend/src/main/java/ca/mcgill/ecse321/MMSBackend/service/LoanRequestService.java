package ca.mcgill.ecse321.MMSBackend.service;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class LoanRequestService {

    @Autowired
    LoanRequestRepository loanRequestRepository;

    /**
     * Create loan request
     */
    @Transactional
    public LoanRequest createLoanRequest(int loanDuration, double fee, Artifact artifact, Client client){
        LoanRequest loanRequest = null;
        if (artifact.getLoanStatus() == Artifact.LoanStatus.Available){
            loanRequest = new LoanRequest();
            loanRequest.setArtifact(artifact);
            loanRequest.setClient(client);
            loanRequest.setLoanDuration(loanDuration);
            loanRequest.setFee(fee);
            loanRequest.setStatus(LoanRequest.LoanStatus.Pending);
            loanRequestRepository.save(loanRequest);
        }
        return loanRequest;
    }

    /**
     * Get loan request
     */
    @Transactional
    public LoanRequest getLoanRequest(Integer requestId){
        return loanRequestRepository.findLoanRequestByRequestId(requestId);
    }

    /**
     * Get list of all loan requests
     */
    @Transactional
    public List<LoanRequest> getAllLoanRequests(){
        return toList(loanRequestRepository.findAll());
    }

    /**
     * Approve loan request
     */
    @Transactional
    public LoanRequest approveLoanRequest(Integer requestId){
        // Checking if loan request exists
        LoanRequest loanRequest=null;
        if (loanRequestRepository.existsById(requestId)){

            // Getting loan request
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            loanRequest.setStatus(LoanRequest.LoanStatus.Approved);
            loanRequest.getArtifact().setLoanStatus(Artifact.LoanStatus.Unavailable);
        }
        return loanRequest;
    }

    /**
     * Reject loan request
     */
    @Transactional
    public LoanRequest rejectLoanRequest(Integer requestId){
        // Checking if loan request exists
        LoanRequest loanRequest=null;
        if (loanRequestRepository.existsById(requestId)){

            // Getting loan request
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            loanRequest.setStatus(LoanRequest.LoanStatus.Rejected);
        }
        return loanRequest;
    }

    /**
     * Confirm artifact's return
     */
    @Transactional
    public LoanRequest returnLoanRequest(Integer requestId){
        // Checking if loan request exists
        LoanRequest loanRequest=null;
        if (loanRequestRepository.existsById(requestId)){

            // Getting loan request
            loanRequest = loanRequestRepository.findLoanRequestByRequestId(requestId);
            loanRequest.setStatus(LoanRequest.LoanStatus.Returned);
            loanRequest.getArtifact().setLoanStatus(Artifact.LoanStatus.Available);
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
