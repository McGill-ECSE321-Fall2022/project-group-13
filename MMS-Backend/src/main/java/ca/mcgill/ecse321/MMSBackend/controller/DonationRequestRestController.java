package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.dto.DonationRequestDto.DonationStatusDto;
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
    public ArtifactDto createDonationArtifact(
        @RequestParam String name, 
        @RequestParam String image,
        @RequestParam String description,
        @RequestParam boolean isDamaged,
        @RequestParam double worth, 
        @RequestParam (name = "mmsSystem") MuseumManagementSystemDto mmsDto)
        
        throws IllegalArgumentException {
        MuseumManagementSystem mms = mmsService
                .getMuseumManagementSystem(mmsDto.getMuseumManagementSystemId());
        Artifact artifact = donationRequestService
                .createDonationArtifact(name, image, description, isDamaged, worth, mms);

        return ToDtoHelper.convertToDto(artifact);
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
    public DonationRequestDto createDonationRequest(
        @RequestParam(name = "client") ClientDto clientDto,
        @RequestParam(name = "artifact") ArtifactDto artifactDto,
        @RequestParam(name = "mmsSystem") MuseumManagementSystemDto museumManagementSystemDto)
        
        throws IllegalArgumentException {
        Client client = clientAccountService.getClient(clientDto.getUsername());
        Artifact artifact = artifactService.getArtifact(artifactDto.getArtifactId());
        MuseumManagementSystem museumManagementSystem = mmsService
                .getMuseumManagementSystem(museumManagementSystemDto.getMuseumManagementSystemId());
        DonationRequest donationRequest = donationRequestService
                .createDonationRequest(client, artifact, museumManagementSystem);

        return ToDtoHelper.convertToDto(donationRequest);
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
    public DonationRequestDto approveDonationRequest(
        @PathVariable("requestId") int requestId,
        
        @RequestParam(name = "room") RoomDto roomDto)

        throws IllegalArgumentException {
        Room room = mmsService.getRoom(roomDto.getRoomId());
        DonationRequest donationRequest = donationRequestService.approveDonationRequest(requestId, room);
        
        return ToDtoHelper.convertToDto(donationRequest);
    }

    /**
     * Rejects a donation request
     *
     * @param requestId
     * @return the rejected donation request dto
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/donationRequest/rejectRequest/{requestId}", "/donationRequest/rejectRequest/{requestId}/" })
    public DonationRequestDto rejectDonationRequest(
        @PathVariable("requestId") int requestId)

        throws IllegalArgumentException {
        DonationRequest donationRequest = donationRequestService.rejectDonationRequest(requestId);
        
        return ToDtoHelper.convertToDto(donationRequest);
    }

    /**
     * Gets a donation request
     *
     * @param requestId
     * @return the donation request dto
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/donationRequest/{requestId}", "/donationRequest/{requestId}/" })
    public DonationRequestDto getDonationRequest(
        @PathVariable("requestId") int requestId)

        throws IllegalArgumentException {
        DonationRequest donationRequest = donationRequestService.getDonationRequest(requestId);
        
        return ToDtoHelper.convertToDto(donationRequest);
    }

    /**
     * Gets all donation requests
     *
     * @return the list of donation request dtos
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/donationRequests", "/donationRequests/" })
    public List<DonationRequestDto> getAllDonationRequests() throws IllegalArgumentException {
        List<DonationRequestDto> donationRequestsDtos = new ArrayList<>();

        for (DonationRequest donationRequest : donationRequestService.getAllDonationRequests()) {
            donationRequestsDtos.add(ToDtoHelper.convertToDto(donationRequest));
        }

        return donationRequestsDtos;
    }

    /**
     * Gets all donation requests registered by its status
     * 
     * @param status
     * @return the list of donation request dtos with the specified status
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/donationRequests/{status}", "/donationRequests/{status}/" })
    public List<DonationRequestDto> getAllDonationRequestsByStatus(
        @PathVariable("status") DonationStatusDto statusDto)

        throws IllegalArgumentException {
        List<DonationRequestDto> donationRequestsDtos = new ArrayList<>();

        for (DonationRequest donationRequest : donationRequestService.getAllDonationRequestsByStatus(ToDtoHelper.convertToDomainObject(statusDto))) {
            donationRequestsDtos.add(ToDtoHelper.convertToDto(donationRequest));
        }

        return donationRequestsDtos;
    }

    /**
     * Gets all donation requests for a client
     *
     * @param clientDto
     * @return the list of donation request dtos of the client
     * @throws IllegalArgumentException
     */
     @GetMapping(value = { "/donationRequests/{client}", "/donationRequests/{client}/" })
     public List<DonationRequestDto> getAllDonationRequestsByClient(
        @PathVariable("client") ClientDto clientDto)

        throws IllegalArgumentException {
        Client client = clientAccountService.getClient(clientDto.getUsername());
        List<DonationRequestDto> donationRequestsDtos = new ArrayList<>();

        for (DonationRequest donationRequest : donationRequestService.getAllDonationRequestsByClient(client)) {
            donationRequestsDtos.add(ToDtoHelper.convertToDto(donationRequest));
        }

        return donationRequestsDtos;
    }

    /**
     * Deletes a donation request
     *
     * @param requestId
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/donationRequest/{requestId}", "/donationRequest/{requestId}/" })
    public void deleteRejectedDonationRequest(
        @PathVariable("requestId") int requestId)

        throws IllegalArgumentException {
        donationRequestService.deleteRejectedDonationRequest(requestId);
    }
    
}
