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
     * Create a donation request
     * 
     * @param artifactId
     * @param roomId
     * @param clientId
     * @return
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

    @Transactional
    public Client getClient(String username){
        Client client = clientRepository.findClientByUsername(username);
        return client;
    }

    @Transactional
    public Room getRoom(int roomId){
        Room room = roomRepository.findRoomByRoomId(roomId);
        return room;
    }

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
    
    @Transactional
    public MuseumManagementSystem getMuseumManagementSystem(int systemId){
        MuseumManagementSystem mms = mmsRepository.findMuseumManagementSystemBySystemId(systemId);
        return mms;
    }

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

    @Transactional
    public DonationRequest getDonationRequest(int requestId) {
        return donationRequestRepository.findDonationRequestByRequestId(requestId);
    }

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

    @Transactional
    public List<DonationRequest> getAllDonationRequestsByStatus(DonationRequest.DonationStatus status) {
        List<DonationRequest> donationRequestsByStatus = new ArrayList<DonationRequest>();
        for (DonationRequest donationRequest : donationRequestRepository.findAll()) {
            if (donationRequest.getStatus().equals(status)) {
                donationRequestsByStatus.add(donationRequest);
            }
        }
        return donationRequestsByStatus;
    }

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
