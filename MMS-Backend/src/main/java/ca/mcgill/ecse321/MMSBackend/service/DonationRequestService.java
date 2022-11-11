package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MMSBackend.dao.ArtifactRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.DonationRequestRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
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
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    MuseumManagementSystemRepository mmsRepository;

    /**
     * Creates an artifact to be donated
     * 
     * @param artifactId
     * @param roomId
     * @param clientId
     * @return artifact object
     * 
     */
    @Transactional
    public Artifact createArtifact(String name, String image, String description, boolean isDamaged, Double worth, MuseumManagementSystem mms) {
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
     * @param loanStatus
     * @param loanFee
     * @return donation request object
     */
    @Transactional
    public DonationRequest approveDonationRequest(int requestId, Room room) {
        DonationRequest donationRequest = null;
        if (!donationRequestRepository.existsById(requestId)) {
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
     * Gets all donation requests registered in the specific museum
     * @param systemId
     * @return a list of donation request objects
     * 
     */
    @Transactional
    public List<DonationRequest> getAllDonationRequestsBySystem(int systemId) {
        List<DonationRequest> donationRequests = new ArrayList<DonationRequest>();
        for (DonationRequest donationRequest : donationRequestRepository.findAll()) {
            if (donationRequest.getMuseumManagementSystem().getSystemId() == systemId) {
                donationRequests.add(donationRequest);
            }
        }
        return donationRequests;
    }

    /**
     * Gets all donation requests registered in the specified museum by its status
     * @param systemId
     * @param status
     * @return a list of donation request objects
     * 
     */
    @Transactional
    public List<DonationRequest> getAllDonationRequestsByStatus(DonationRequest.DonationStatus status, int systemId) {
        List<DonationRequest> donationRequestsByStatus = new ArrayList<DonationRequest>();
        for (DonationRequest donationRequest : donationRequestRepository.findAll()) {
            if (donationRequest.getStatus().equals(status) && donationRequest.getMuseumManagementSystem().getSystemId() == systemId) {
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
        List<DonationRequest> donationRequestsByClient = new ArrayList<DonationRequest>();
        for (DonationRequest donationRequest : donationRequestRepository.findAll()) {
            if (donationRequest.getClient().equals(client)) {
                donationRequestsByClient.add(donationRequest);
            }
        }
        return donationRequestsByClient;
    }

}
