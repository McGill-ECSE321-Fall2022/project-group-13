package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.dto.LoanRequestDto;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.LoanRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
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

    @Autowired
    private ToDtoHelper toDtoHelper;


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
        return new ResponseEntity<>(toDtoHelper.convertToDto(loanRequest), HttpStatus.CREATED);
    }

    /**
     * Gets a loan request by its id
     * @param requestId
     * @return loan request object
     */
    @GetMapping(value = {"/loanRequest/{requestId}", "/loanRequest/{requestId}/"})
    public ResponseEntity<LoanRequestDto> getLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.getLoanRequest(requestId);
        return new ResponseEntity<>(toDtoHelper.convertToDto(loanRequest), HttpStatus.OK);
    }

    /**
     * Gets all loan request
     * @return loan request object
     */
    @GetMapping(value = {"/loanRequests", "/loanRequests/"})
    public ResponseEntity<List<LoanRequestDto>> getAllLoanRequests() {
        return new ResponseEntity<>(loanRequestService.getAllLoanRequests().stream().map(toDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
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
        for (LoanRequest loanRequest : loanRequestService.getAllLoanRequestsByStatus(toDtoHelper.convertStringToLoanStatus(status))) {
            loanRequestsDtos.add(toDtoHelper.convertToDto(loanRequest));
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
            loanRequestsByStatusDto.add(toDtoHelper.convertToDto(loanRequest));
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
            loanRequestsByClientDto.add(toDtoHelper.convertToDto(loanRequest));
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
        return new ResponseEntity<>(toDtoHelper.convertToDto(loanRequest), HttpStatus.OK);
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
        return new ResponseEntity<>(toDtoHelper.convertToDto(loanRequest), HttpStatus.OK);
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
        return new ResponseEntity<>(toDtoHelper.convertToDto(loanRequest), HttpStatus.OK);
    }
}
