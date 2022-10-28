package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.LoanRequest;

/**
 * @author Nazia Chowdhury
 */
public interface LoanRequestRepository extends CrudRepository<LoanRequest, Integer> {
    public LoanRequest findLoanRequestByRequestId(Integer id);
}
