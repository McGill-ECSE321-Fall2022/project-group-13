package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.service.ArtifactService;
import ca.mcgill.ecse321.MMSBackend.service.ClientAccountService;
import ca.mcgill.ecse321.MMSBackend.service.LoanRequestService;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nazia Chowdhury (naziaC)
 * The LoanRequestRestController class is responsible for exposing
 * the business logic declared in LoanRequestService using a REST
 * API.
 */
@CrossOrigin(origins = "*")
@RestController
public class LoanRequestRestController {

    @Autowired
    private LoanRequestService loanRequestService;
    @Autowired
    private ArtifactService artifactService;
    @Autowired
    private ClientAccountService clientAccountService;
    @Autowired
    private MuseumManagementSystemService mmsService;
    
    /**
     * Create a new loan request
     * @param loanDuration
     * @param artifactId
     * @param username
     * @param systemId
     */
    @PostMapping(value = {"/loanRequest", "/loanRequest/"})
    public ResponseEntity<LoanRequestDto> createLoanRequest(@RequestParam int loanDuration, @RequestParam int artifactId,
            @RequestParam String username, @RequestParam int systemId) throws IllegalArgumentException {
        Artifact artifact = artifactService.getArtifact(artifactId);
        Client client = clientAccountService.getClient(username);
        MuseumManagementSystem mms = mmsService.getMuseumManagementSystem(systemId);
        LoanRequest loanRequest = loanRequestService.createLoanRequest(loanDuration, artifact, client, mms);
        return new ResponseEntity<>(convertToDto(loanRequest), HttpStatus.CREATED);
    }

    /**
     * Gets a loan request by its id
     * @param requestId
     * @return loan request object
     */
    @GetMapping(value = {"/loanRequest/{requestId}", "/loanRequest/{requestId}/"})
    public ResponseEntity<LoanRequestDto> getLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.getLoanRequest(requestId);
        return new ResponseEntity<>(convertToDto(loanRequest), HttpStatus.OK);
    }

    /**
     * Gets all loan request
     * @return loan request object
     */
    @GetMapping(value = {"/loanRequests", "/loanRequests/"})
    public ResponseEntity<List<LoanRequestDto>> getAllLoanRequests() {
        return new ResponseEntity<>(loanRequestService.getAllLoanRequests().stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Gets all loan requests registered by its status
     *
     * @param status
     * @return the list of loan request dtos with the specified status
     * @throws IllegalArgumentException
     */
    @GetMapping(value = {"/loanRequests/Status/{status}", "/loanRequests/Status/{status}/"})
    public ResponseEntity<List<LoanRequestDto>> getAllLoanRequestsByStatus(@PathVariable("status") String status) throws IllegalArgumentException {
        List<LoanRequestDto> loanRequestsDtos = new ArrayList<>();
        for (LoanRequest loanRequest : loanRequestService.getAllLoanRequestsByStatus(convertStringToLoanStatus(status))) {
            loanRequestsDtos.add(convertToDto(loanRequest));
        }
        return new ResponseEntity<>(loanRequestsDtos, HttpStatus.OK);
    }

    /**
     * Gets all loan requests than can be approved/rejected
     *
     * @return a list of loan request objects for a specific client than can be approved/rejected
     */
    @GetMapping(value = {"/activeLoanRequests", "/activeLoanRequests/"})
    public ResponseEntity<List<LoanRequestDto>> getAllActiveLoanRequests() throws IllegalArgumentException {
        List<LoanRequestDto> loanRequestsByStatusDto = new ArrayList<>();
        for (LoanRequest loanRequest : loanRequestService.getAllActiveLoanRequests()) {
            loanRequestsByStatusDto.add(convertToDto(loanRequest));
        }
        return new ResponseEntity<>(loanRequestsByStatusDto, HttpStatus.OK);
    }

    /**
     * Gets all loan requests registered by its client
     *
     * @param username
     * @return a list of loan request objects
     */
    @GetMapping(value = {"/loanRequests/{client}", "/loanRequests/{client}/"})
    public ResponseEntity<List<LoanRequestDto>> getAllLoanRequestsByClient(@PathVariable(name="client") String username) throws IllegalArgumentException {
        Client client = clientAccountService.getClient(username);
        List<LoanRequestDto> loanRequestsByClientDto = new ArrayList<>();
        for (LoanRequest loanRequest : loanRequestService.getAllLoanRequestsByClient(client)) {
            loanRequestsByClientDto.add(convertToDto(loanRequest));
        }
        return new ResponseEntity<>(loanRequestsByClientDto, HttpStatus.OK);
    }

    /**
     * Approve loan request
     *
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = {"/loanRequest/approveRequest/{requestId}", "/loanRequest/approveRequest/{requestId}/"})
    public ResponseEntity<LoanRequestDto> approveLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.approveLoanRequest(requestId);
        return new ResponseEntity<>(convertToDto(loanRequest), HttpStatus.OK);
    }

    /**
     * Reject loan request
     *
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = {"/loanRequest/rejectRequest/{requestId}", "/loanRequest/rejectRequest/{requestId}/"})
    public ResponseEntity<LoanRequestDto> rejectLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.rejectLoanRequest(requestId);
        return new ResponseEntity<>(convertToDto(loanRequest), HttpStatus.OK);
    }

    /**
     * Confirm artifact's return
     *
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = {"/loanRequest/loanReturn/{requestId}", "/loanRequest/loanReturn/{requestId}/"})
    public ResponseEntity<LoanRequestDto> returnLoanedArtifact(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.returnLoanedArtifact(requestId);
        return new ResponseEntity<>(convertToDto(loanRequest), HttpStatus.OK);
    }

    // Helper methods to convert classes from the model into DTOs and vice-versa

    private LoanRequestDto convertToDto(LoanRequest loanRequest) {
        if (loanRequest == null) {
            throw new IllegalArgumentException("Loan request cannot be null!");
        }
        return new LoanRequestDto(loanRequest.getRequestId(),
                loanRequest.getLoanDuration(),
                loanRequest.getFee(),
                convertToDto(loanRequest.getClient()),
                convertToDto(loanRequest.getArtifact()),
                convertToDto(loanRequest.getStatus()),
                convertToDto(loanRequest.getMuseumManagementSystem()));
    }

    private LoanRequestDto.LoanRequestStatusDto convertToDto(LoanRequest.LoanStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return switch (status) {
            case Approved -> LoanRequestDto.LoanRequestStatusDto.Approved;
            case Rejected -> LoanRequestDto.LoanRequestStatusDto.Rejected;
            case Pending -> LoanRequestDto.LoanRequestStatusDto.Pending;
            case Returned -> LoanRequestDto.LoanRequestStatusDto.Returned;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    private LoanRequest.LoanStatus convertStringToLoanStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return switch (status) {
            case "Approved" -> LoanRequest.LoanStatus.Approved;
            case "Rejected" -> LoanRequest.LoanStatus.Rejected;
            case "Pending" -> LoanRequest.LoanStatus.Pending;
            case "Returned" -> LoanRequest.LoanStatus.Returned;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    private Artifact.LoanStatus convertArtifactStringToLoanStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return switch (status) {
            case "Available" -> Artifact.LoanStatus.Available;
            case "Unavailable" -> Artifact.LoanStatus.Unavailable;
            case "Loaned" -> Artifact.LoanStatus.Loaned;
            default -> null;
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

    private MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms) {
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        return new MuseumManagementSystemDto(mms.getSystemId(), mms.getName(), mms.getOpenTime(), mms.getCloseTime(), mms.getMaxLoanNumber(), mms.getTicketFee());
    }

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
}
