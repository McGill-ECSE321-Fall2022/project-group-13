package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.DonationRequest;

public interface DonationRequestRepository extends CrudRepository<DonationRequest, Integer> {
    public DonationRequest findDonationRequestByRequestId(Integer id);
}
