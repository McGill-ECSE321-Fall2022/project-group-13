package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MMSBackend.dao.ArtifactRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.DonationRequestRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.DonationRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;
import ca.mcgill.ecse321.MMSBackend.model.Artifact.LoanStatus;

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
     * Gets a single client by its username
     * @param username
     * @return client object
     * 
     */
    @Transactional
    public Client getClient(String username){
        Client client = clientRepository.findClientByUsername(username);
        return client;
    }

    /**
     * Gets a single room by its id to store/display the donated artifact
     * @param roomId
     * @return room object
     * 
     */
    @Transactional
    public Room getRoom(int roomId){
        Room room = roomRepository.findRoomByRoomId(roomId);
        return room;
    }

    /**
     * Gets all rooms registered in the specific museum
     * @param systemId
     * @return a list of room objects
     * 
     */
    @Transactional
    public List<Room> getAllRoomsBySystem(int systemId){
        List<Room> rooms = new ArrayList<Room>();
        for (Room room : roomRepository.findAll()){
            if (room.getMuseumManagementSystem().getSystemId() == systemId){
                rooms.add(room);
            }
        }
        return rooms;
    }
    
    /**
     * Gets the museum management system by its id
     * @param systemId
     * @return museum management system object
     * 
     */
    @Transactional
    public MuseumManagementSystem getMuseumManagementSystem(int systemId){
        MuseumManagementSystem mms = mmsRepository.findMuseumManagementSystemBySystemId(systemId);
        return mms;
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
    public DonationRequest approveDonationRequest(int requestId, Room room, LoanStatus loanStatus, double loanFee) {
        DonationRequest donationRequest = null;
        if (donationRequestRepository.existsById(requestId)) {
            donationRequest = donationRequestRepository.findDonationRequestByRequestId(requestId);
            Artifact artifact = donationRequest.getArtifact();

            artifact.setRoomLocation(room);
            artifact.setLoanStatus(loanStatus);
            artifact.setLoanFee(loanFee);

            artifactRepository.save(artifact);

            donationRequest.setStatus(DonationRequest.DonationStatus.Approved);

            donationRequestRepository.save(donationRequest);
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
