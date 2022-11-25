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
     * @param systemId
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

        return new ResponseEntity<>(convertToDto(artifact), HttpStatus.CREATED);
    }

     /**
      * Creates a new donation request
      *
      * @param clientUsername
      * @param artifactId
      * @param systemId
      * @return the created donation request dto
      * @throws IllegalArgumentException
      */
    @PostMapping(value = { "/donationRequest", "/donationRequest/" })
    public ResponseEntity<DonationRequestDto> createDonationRequest(@RequestParam String clientUsername,
        @RequestParam int artifactId, @RequestParam int systemId) throws IllegalArgumentException {
        Client client = clientAccountService.getClient(clientUsername);
        Artifact artifact = artifactService.getArtifact(artifactId);
        MuseumManagementSystem museumManagementSystem = mmsService.getMuseumManagementSystem(systemId);
        DonationRequest donationRequest = donationRequestService.createDonationRequest(client, artifact, museumManagementSystem);

        return new ResponseEntity<>(convertToDto(donationRequest), HttpStatus.CREATED);
    }

    /**
     * Approves a donation request
     *
     * @param requestId
     * @param roomId
     * @return the approved donation request dto
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/donationRequest/approveRequest/{requestId}", "/donationRequest/approveRequest/{requestId}/" })
    public ResponseEntity<DonationRequestDto> approveDonationRequest(@PathVariable("requestId") int requestId,
            @RequestParam int roomId) throws IllegalArgumentException {
        Room room = mmsService.getRoom(roomId);
        DonationRequest donationRequest = donationRequestService.approveDonationRequest(requestId, room);
        
        return new ResponseEntity<>(convertToDto(donationRequest), HttpStatus.OK);
    }

    /**
     * Rejects a donation request
     *
     * @param requestId
     * @return the rejected donation request dto
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/donationRequest/rejectRequest/{requestId}", "/donationRequest/rejectRequest/{requestId}/" })
    public ResponseEntity<DonationRequestDto> rejectDonationRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        DonationRequest donationRequest = donationRequestService.rejectDonationRequest(requestId);

        return new ResponseEntity<>(convertToDto(donationRequest), HttpStatus.OK);
    }

    /**
     * Gets a donation request
     *
     * @param requestId
     * @return the donation request dto
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/donationRequest/{requestId}", "/donationRequest/{requestId}/" })
    public ResponseEntity<DonationRequestDto> getDonationRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        DonationRequest donationRequest = donationRequestService.getDonationRequest(requestId);

        return new ResponseEntity<>(convertToDto(donationRequest), HttpStatus.OK);
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
            donationRequestsDtos.add(convertToDto(donationRequest));
        }

        return new ResponseEntity<>(donationRequestsDtos, HttpStatus.OK);
    }

    /**
     * Gets all donation requests registered by its status
     * 
     * @param status
     * @return the list of donation request dtos with the specified status
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/donationRequests/withRequestStatus/{status}", "/donationRequests/{status}/" })
    public ResponseEntity<List<DonationRequestDto>> getAllDonationRequestsByStatus(@PathVariable("status") String status) throws IllegalArgumentException {
        List<DonationRequestDto> donationRequestsDtos = new ArrayList<>();

        for (DonationRequest donationRequest : donationRequestService.getAllDonationRequestsByStatus(convertStringToDonationStatus(status))) {
            donationRequestsDtos.add(convertToDto(donationRequest));
        }

        return new ResponseEntity<>(donationRequestsDtos, HttpStatus.OK);
    }

    /**
     * Gets all donation requests for a client
     *
     * @param clientUsername
     * @return the list of donation request dtos of the client
     * @throws IllegalArgumentException
     */
     @GetMapping(value = { "/donationRequests/ofClient/{client}", "/donationRequests/{client}/" })
     public ResponseEntity<List<DonationRequestDto>> getAllDonationRequestsByClient(
        @PathVariable("client") String clientUsername)

        throws IllegalArgumentException {
        Client client = clientAccountService.getClient(clientUsername);
        List<DonationRequestDto> donationRequestsDtos = new ArrayList<>();

        for (DonationRequest donationRequest : donationRequestService.getAllDonationRequestsByClient(client)) {
            donationRequestsDtos.add(convertToDto(donationRequest));
        }

        return new ResponseEntity<>(donationRequestsDtos, HttpStatus.OK);
    }

    /**
     * Deletes a donation request
     *
     * @param requestId
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/donationRequest/delete/{requestId}", "/donationRequest/{requestId}/" })
    public ResponseEntity<Boolean> deleteRejectedDonationRequest(
        @PathVariable("requestId") int requestId)

        throws IllegalArgumentException {
        boolean response = donationRequestService.deleteRejectedDonationRequest(requestId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Helper methods to convert classes from the model into DTOs and vice-versa

    private ArtifactDto convertToDto(Artifact a) {
        if (a == null) {
            throw new IllegalArgumentException("There is no such Artifact!");
        }
        RoomDto roomDto = convertToDto(a.getRoomLocation());
        MuseumManagementSystemDto mmsDto = convertToDto(a.getMuseumManagementSystem());
        ArtifactDto.LoanStatusDto statusDto = convertToDto(a.getLoanStatus());

        return new ArtifactDto(a.getArtifactId(), a.getName(), a.getImage(), a.getDescription(),
                statusDto, a.getIsDamaged(), a.getLoanFee(), a.getWorth(), roomDto, mmsDto);
    }

    private RoomDto convertToDto(Room r) {
        if (r == null) {
            return null;
        }
        MuseumManagementSystemDto systemDto = convertToDto(r.getMuseumManagementSystem());
        RoomDto.RoomTypeDto typeDto = convertToDto(r.getType());
        return new RoomDto(r.getRoomId(), r.getName(), typeDto, systemDto);
    }

    private MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms) {
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        return new MuseumManagementSystemDto(mms.getSystemId(), mms.getName(), mms.getOpenTime(), mms.getCloseTime(), mms.getMaxLoanNumber(), mms.getTicketFee());
    }

    private ArtifactDto.LoanStatusDto convertToDto(Artifact.LoanStatus status) {
        if (status == null) {
            return null;
        }

        return switch (status) {
            case Available -> ArtifactDto.LoanStatusDto.Available;
            case Unavailable -> ArtifactDto.LoanStatusDto.Unavailable;
            case Loaned -> ArtifactDto.LoanStatusDto.Loaned;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    private RoomDto.RoomTypeDto convertToDto(Room.RoomType type) {
        if (type == null) {
            throw new IllegalArgumentException("Room type cannot be null.");
        }

        return switch (type) {
            case Small -> RoomDto.RoomTypeDto.Small;
            case Large -> RoomDto.RoomTypeDto.Large;
            case Storage -> RoomDto.RoomTypeDto.Storage;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }

    private DonationRequestDto convertToDto(DonationRequest donationRequest) {
        if (donationRequest == null) {
            throw new IllegalArgumentException("Donation request cannot be null!");
        }

        return new DonationRequestDto(donationRequest.getRequestId(),
                convertToDto(donationRequest.getClient()),
                convertToDto(donationRequest.getArtifact()),
                convertToDto(donationRequest.getStatus()),
                convertToDto(donationRequest.getMuseumManagementSystem()));
    }

    private DonationRequest.DonationStatus convertStringToDonationStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return switch (status) {
            case "Pending" -> DonationRequest.DonationStatus.Pending;
            case "Approved" -> DonationRequest.DonationStatus.Approved;
            case "Rejected" -> DonationRequest.DonationStatus.Rejected;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    private DonationRequestDto.DonationStatusDto convertToDto(DonationRequest.DonationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("DonationRequestStatus cannot be null!");
        }

        return switch (status) {
            case Approved -> DonationRequestDto.DonationStatusDto.Approved;
            case Rejected -> DonationRequestDto.DonationStatusDto.Rejected;
            case Pending -> DonationRequestDto.DonationStatusDto.Pending;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    private ClientDto convertToDto(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("There is no such Client!");
        }
        MuseumManagementSystemDto mmsDto = convertToDto(client.getMuseumManagementSystem());

        return new ClientDto(client.getUsername(), client.getName(), client.getPassword(),
                client.getCurrentLoanNumber(), mmsDto);
    }
}
