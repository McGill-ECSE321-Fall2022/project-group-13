package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.service.ArtifactService;
import ca.mcgill.ecse321.MMSBackend.service.ClientAccountService;
import ca.mcgill.ecse321.MMSBackend.service.DonationRequestService;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;

/**
 * @author Yu An Lu (yu-an-lu)
 * 
 * The DonationRequestRestController class exposes the business logic declared 
 * in DonationRequestService using REST API.
 * 
 */
@CrossOrigin(origins = "*")
@RestController
public class DonationRequestRestController {

    @Autowired
    private DonationRequestService donationRequestService;

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private ClientAccountService clientAccountService;

    @Autowired
    private MuseumManagementSystemService mmsService;

    /**
     * Creates a new artifact to be donated
     * 
     * @param name
     * @param image
     * @param description
     * @param isDamaged
     * @param worth
     * @param mmsDto
     * @return the created artifact for donation
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/donationArtifact", "/donationArtifact/" })
    public ResponseEntity<ArtifactDto> createDonationArtifact(
        @RequestParam String name, 
        @RequestParam String image,
        @RequestParam String description,
        @RequestParam boolean isDamaged,
        @RequestParam double worth, 
        @RequestParam int systemId)
        
        throws IllegalArgumentException {
        MuseumManagementSystem mms = mmsService.getMuseumManagementSystem(systemId);
        Artifact artifact = donationRequestService.createDonationArtifact(name, image, description, isDamaged, worth, mms);

        return new ResponseEntity<ArtifactDto> (ToDtoHelper.convertToDto(artifact), HttpStatus.CREATED);
    }

     /**
      * Creates a new donation request
      *
      * @param clientDto
      * @param artifactDto
      * @param museumManagementSystemDto
      * @return the created donation request dto
      * @throws IllegalArgumentException
      */
    @PostMapping(value = { "/donationRequest", "/donationRequest/" })
    public ResponseEntity<DonationRequestDto> createDonationRequest(
        @RequestParam String clientUsername,
        @RequestParam int artifactId,
        @RequestParam int systemId)
        
        throws IllegalArgumentException {
        Client client = clientAccountService.getClient(clientUsername);
        Artifact artifact = artifactService.getArtifact(artifactId);
        MuseumManagementSystem museumManagementSystem = mmsService.getMuseumManagementSystem(systemId);
        DonationRequest donationRequest = donationRequestService.createDonationRequest(client, artifact, museumManagementSystem);

        return new ResponseEntity<DonationRequestDto> (ToDtoHelper.convertToDto(donationRequest), HttpStatus.CREATED);
    }

    /**
     * Approves a donation request
     *
     * @param requestId
     * @param roomDto
     * @return the approved donation request dto
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/donationRequest/approveRequest/{requestId}", "/donationRequest/approveRequest/{requestId}/" })
    public ResponseEntity<DonationRequestDto> approveDonationRequest(
        @PathVariable("requestId") int requestId,
        
        @RequestParam int roomId)

        throws IllegalArgumentException {
        Room room = mmsService.getRoom(roomId);
        DonationRequest donationRequest = donationRequestService.approveDonationRequest(requestId, room);
        
        return new ResponseEntity<DonationRequestDto> (ToDtoHelper.convertToDto(donationRequest), HttpStatus.OK);
    }

    /**
     * Rejects a donation request
     *
     * @param requestId
     * @return the rejected donation request dto
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/donationRequest/rejectRequest/{requestId}", "/donationRequest/rejectRequest/{requestId}/" })
    public ResponseEntity<DonationRequestDto> rejectDonationRequest(
        @PathVariable("requestId") int requestId)

        throws IllegalArgumentException {
        DonationRequest donationRequest = donationRequestService.rejectDonationRequest(requestId);
        
        return new ResponseEntity<DonationRequestDto> (ToDtoHelper.convertToDto(donationRequest), HttpStatus.OK);
    }

    /**
     * Gets a donation request
     *
     * @param requestId
     * @return the donation request dto
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/donationRequest/{requestId}", "/donationRequest/{requestId}/" })
    public ResponseEntity<DonationRequestDto> getDonationRequest(
        @PathVariable("requestId") int requestId)

        throws IllegalArgumentException {
        DonationRequest donationRequest = donationRequestService.getDonationRequest(requestId);
        
        return new ResponseEntity<DonationRequestDto> (ToDtoHelper.convertToDto(donationRequest), HttpStatus.OK);
    }

    /**
     * Gets all donation requests
     *
     * @return the list of donation request dtos
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/donationRequests", "/donationRequests/" })
    public ResponseEntity<List<DonationRequestDto>> getAllDonationRequests() throws IllegalArgumentException {
        List<DonationRequestDto> donationRequestsDtos = new ArrayList<>();

        for (DonationRequest donationRequest : donationRequestService.getAllDonationRequests()) {
            donationRequestsDtos.add(ToDtoHelper.convertToDto(donationRequest));
        }

        return new ResponseEntity<List<DonationRequestDto>> (donationRequestsDtos, HttpStatus.OK);
    }

    /**
     * Gets all donation requests registered by its status
     * 
     * @param status
     * @return the list of donation request dtos with the specified status
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/donationRequests/{status}", "/donationRequests/{status}/" })
    public ResponseEntity<List<DonationRequestDto>> getAllDonationRequestsByStatus(
        @PathVariable("status") String status)

        throws IllegalArgumentException {
        List<DonationRequestDto> donationRequestsDtos = new ArrayList<>();

        for (DonationRequest donationRequest : donationRequestService.getAllDonationRequestsByStatus(ToDtoHelper.convertStringToDonationStatus(status))) {
            donationRequestsDtos.add(ToDtoHelper.convertToDto(donationRequest));
        }

        return new ResponseEntity<List<DonationRequestDto>> (donationRequestsDtos, HttpStatus.OK);
    }

    /**
     * Gets all donation requests for a client
     *
     * @param clientDto
     * @return the list of donation request dtos of the client
     * @throws IllegalArgumentException
     */
     @GetMapping(value = { "/donationRequests/{client}", "/donationRequests/{client}/" })
     public ResponseEntity<List<DonationRequestDto>> getAllDonationRequestsByClient(
        @PathVariable("client") String clientUsername)

        throws IllegalArgumentException {
        Client client = clientAccountService.getClient(clientUsername);
        List<DonationRequestDto> donationRequestsDtos = new ArrayList<>();

        for (DonationRequest donationRequest : donationRequestService.getAllDonationRequestsByClient(client)) {
            donationRequestsDtos.add(ToDtoHelper.convertToDto(donationRequest));
        }

        return new ResponseEntity<List<DonationRequestDto>> (donationRequestsDtos, HttpStatus.OK);
    }

    /**
     * Deletes a donation request
     *
     * @param requestId
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/donationRequest/{requestId}", "/donationRequest/{requestId}/" })
    public ResponseEntity<Boolean> deleteRejectedDonationRequest(
        @PathVariable("requestId") int requestId)

        throws IllegalArgumentException {
        boolean response = donationRequestService.deleteRejectedDonationRequest(requestId);

        return new ResponseEntity<Boolean> (response, HttpStatus.OK);
    }
    
}
