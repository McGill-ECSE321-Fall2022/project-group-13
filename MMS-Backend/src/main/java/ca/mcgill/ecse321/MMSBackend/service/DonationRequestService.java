package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MMSBackend.dao.ArtifactRepository;
import ca.mcgill.ecse321.MMSBackend.dao.DonationRequestRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.DonationRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;

/**
 * @author Yu An Lu (yu-an-lu)
 * The DonationRequestService class implements the use case based on the requirement:
 * 
 * “FREQ09: The museum management system shall allow clients to make an artifact donation request to the museum 
 * by recording the name of the artifact, a picture, a description, the worth and allow the manager and the employees 
 * to modify the donation request status from pending to either approved or rejected.”
 * 
 */
@Service
public class DonationRequestService {

    @Autowired
    DonationRequestRepository donationRequestRepository;
    @Autowired
    ArtifactRepository artifactRepository;

    /**
     * Creates an artifact to be donated
     * @param name
     * @param image
     * @param description
     * @param isDamaged
     * @param worth
     * @param mms
     * @return artifact object
     * 
     */
    @Transactional
    public Artifact createArtifact(String name, String image, String description, boolean isDamaged, double worth, MuseumManagementSystem mms) {
        if (name == null || image == null || description == null || mms == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values not allowed");

        Artifact artifact = new Artifact();
        artifact.setName(name);
        artifact.setImage(image);
        artifact.setDescription(description);
        artifact.setIsDamaged(isDamaged);
        artifact.setWorth(worth);
        artifact.setMuseumManagementSystem(mms);

        artifactRepository.save(artifact);
        return artifact;
    }

    /**
     * Creates a donation request pending for approval
     * @param client
     * @param artifact
     * @param mms
     * @return donation request object
     * 
     */
    @Transactional
    public DonationRequest createDonationRequest(Client client, Artifact artifact, MuseumManagementSystem mms) {
        if (client == null || artifact == null || mms == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values not allowed");

        DonationRequest donationRequest = new DonationRequest();
        donationRequest.setClient(client);
        donationRequest.setArtifact(artifact);
        donationRequest.setMuseumManagementSystem(mms);
        donationRequest.setStatus(DonationRequest.DonationStatus.Pending);

        donationRequestRepository.save(donationRequest);
        return donationRequest;
    }

    /**
     * Approves a donation request and stores the donated artifact in the specified room
     * @param requestId
     * @param room
     * @return donation request object
     */
    @Transactional
    public DonationRequest approveDonationRequest(int requestId, Room room) {
        if (room == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values not allowed");

        DonationRequest donationRequest = null;
        if (donationRequestRepository.existsById(requestId)) {
            donationRequest = donationRequestRepository.findDonationRequestByRequestId(requestId);
            Artifact artifact = donationRequest.getArtifact();

            artifact.setRoomLocation(room);
            artifact.setLoanStatus(Artifact.LoanStatus.Unavailable);

            artifactRepository.save(artifact);

            donationRequest.setStatus(DonationRequest.DonationStatus.Approved);

            donationRequestRepository.save(donationRequest);
        } else {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Donation request not found");
        }
        return donationRequest;
    }

    /**
     * Rejects a donation request
     * @param requestId
     * @return donation request object
     * 
     */
    @Transactional
    public DonationRequest rejectDonationRequest(int requestId) {
        DonationRequest donationRequest = null;
        if (donationRequestRepository.existsById(requestId)) {
            donationRequest = donationRequestRepository.findDonationRequestByRequestId(requestId);
            donationRequest.setStatus(DonationRequest.DonationStatus.Rejected);

            donationRequestRepository.save(donationRequest);
        } else {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Donation request not found");
        }
        
        return donationRequest;
    }

    /**
     * Gets a donation request by its id
     * @param requestId
     * @return donation request object
     * 
     */
    @Transactional
    public DonationRequest getDonationRequest(int requestId) {
        if (!donationRequestRepository.existsById(requestId)) 
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Donation request not found");

        return donationRequestRepository.findDonationRequestByRequestId(requestId);
    }

    /**
     * Gets all donation requests
     * @return a list of donation request objects
     * 
     */
    @Transactional
    public List<DonationRequest> getAllDonationRequests() {
        return toList(donationRequestRepository.findAll());
    }

    /**
     * Gets all donation requests registered by its status
     * @param status
     * @return a list of donation request objects
     * 
     */
    @Transactional
    public List<DonationRequest> getAllDonationRequestsByStatus(DonationRequest.DonationStatus status) {
        if (status == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values not allowed");

        List<DonationRequest> donationRequestsByStatus = new ArrayList<DonationRequest>();
        for (DonationRequest donationRequest : donationRequestRepository.findAll()) {
            if (donationRequest.getStatus().equals(status)) {
                donationRequestsByStatus.add(donationRequest);
            }
        }
        return donationRequestsByStatus;
    }

    /**
     * Gets all donation requests registered by its client
     * @param client
     * @return a list of donation request objects
     * 
     */
    @Transactional
    public List<DonationRequest> getAllDonationRequestsByClient(Client client) {
        if (client == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values not allowed");

        List<DonationRequest> donationRequestsByClient = new ArrayList<DonationRequest>();
        for (DonationRequest donationRequest : donationRequestRepository.findAll()) {
            if (donationRequest.getClient().equals(client)) {
                donationRequestsByClient.add(donationRequest);
            }
        }
        return donationRequestsByClient;
    }

    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

}
