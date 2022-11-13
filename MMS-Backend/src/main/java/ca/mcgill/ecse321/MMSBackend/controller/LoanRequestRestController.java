package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.service.AccountManagementService;
import ca.mcgill.ecse321.MMSBackend.service.ArtifactService;
import ca.mcgill.ecse321.MMSBackend.service.LoanRequestService;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Nazia Chowdhury (naziaC)
 *         The LoanRequestRestController class is responsible for exposing
 *         the business logic declared in LoanRequestService using a REST
 *         API.
 */
@CrossOrigin(origins = "*")
@RestController
public class LoanRequestRestController {

    @Autowired
    private LoanRequestService loanRequestService;
    @Autowired
    private ArtifactService artifactService;
    @Autowired
    private AccountManagementService accountManagementService;
    @Autowired
    private MuseumManagementSystemService mmsService;

    /**
     * Create a new loan request
     */
    @PostMapping(value = { "/loanRequest", "/loanRequest/" })
    public LoanRequestDto createLoanRequest(@RequestParam(name = "loanDuration") int loanDuration,
           @RequestParam(name = "fee") int fee, @RequestParam(name="artifact") ArtifactDto artifactDto,
           @RequestParam(name="client") ClientDto clientDto, @RequestParam(name="mmsSystem") MuseumManagementSystemDto museumManagementSystemDto) throws IllegalArgumentException {

        Artifact artifact = artifactService.getArtifact(artifactDto.getArtifactId());
        Client client = accountManagementService.getClient(clientDto.getUsername());
        MuseumManagementSystem museumManagementSystem = mmsService.getMuseumManagementSystem(museumManagementSystemDto.getMuseumManagementSystemId());
        LoanRequest loanRequest = loanRequestService.createLoanRequest(loanDuration, fee, artifact, client, museumManagementSystem);

        return ToDtoHelper.convertToDto(loanRequest);
    }

    /**
     * Gets a loan request by its id
     * @param requestId
     * @return loan request object
     */
    @GetMapping(value = { "/loanRequest/{id}", "/loanRequest/{id}/" })
    public LoanRequestDto getLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException
    {
        LoanRequest loanRequest  = loanRequestService.getLoanRequest(requestId);
        return ToDtoHelper.convertToDto(loanRequest);
    }

    // TODO ask about id for system
    /**
     * Gets all loan requests registered in the specific museum
     * @param museumManagementSystemDto
     * @return a list of loan request objects
     */
    @GetMapping(value = { "/loanRequests/{mmsSystem}", "/loanRequests/{mmsSystem}/" })
    public List<LoanRequestDto> getAllLoanRequestsBySystem(@RequestParam(name="mmsSystem") MuseumManagementSystemDto museumManagementSystemDto) throws IllegalArgumentException {
        MuseumManagementSystem museumManagementSystem = mmsService.getMuseumManagementSystem(museumManagementSystemDto.getMuseumManagementSystemId());
        List<LoanRequestDto> loanRequestsDto = new ArrayList<LoanRequestDto>();
        for (LoanRequest loanRequest : loanRequestService.getAllLoanRequestsBySystem(museumManagementSystem.getSystemId())) {
            if (loanRequest.getMuseumManagementSystem().getSystemId() == museumManagementSystem.getSystemId()) {
                loanRequestsDto.add(ToDtoHelper.convertToDto(loanRequest));
            }
        }
        return loanRequestsDto;
    }

    @GetMapping(value = { "/loanRequests", "/loanRequests/" })
    public List<LoanRequestDto> getAllLoanRequests(){
        return loanRequestService.getAllLoanRequests().stream().map(p -> ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    /**
     * Gets all loan requests registered in the specified museum by its status
     * @param status
     * @return a list of loan request objects that have a specific status
     */
    @GetMapping(value = { "/loanRequest/{status}", "/loanRequest/{status}/" })
    public List<LoanRequestDto> getAllLoanRequestsBySystem(@RequestParam(name="status") LoanRequest.LoanStatus status, @RequestParam(name="mmsSystem") MuseumManagementSystemDto museumManagementSystemDto) {
        MuseumManagementSystem museumManagementSystem = mmsService.getMuseumManagementSystem(museumManagementSystemDto.getMuseumManagementSystemId());
        List<LoanRequestDto> loanRequestsDto = new ArrayList<LoanRequestDto>();
        for (LoanRequest loanRequest : loanRequestService.getAllLoanRequestsByStatus(status, museumManagementSystem.getSystemId())) {
            if (loanRequest.getMuseumManagementSystem().getSystemId() == museumManagementSystem.getSystemId()) {
                loanRequestsDto.add(ToDtoHelper.convertToDto(loanRequest));
            }
        }
        return loanRequestsDto;
    }

    // TODO ask for this one -> same mapping as getAllLoanRequestsBySystem
    /**
     * Gets all loan requests than can be approved/rejected
     * @param museumManagementSystemDto
     * @return a list of loan request objects for a specific client than can be approved/rejected
     */
    @GetMapping(value = { "/activeLoanRequest/{mmsSystem}", "/activeLoanRequest/{mmsSystem}/" })
    public List<LoanRequestDto> getAllActiveLoanRequests(@RequestParam(name="mmsSystem") MuseumManagementSystemDto museumManagementSystemDto) throws IllegalArgumentException {
        MuseumManagementSystem museumManagementSystem = mmsService.getMuseumManagementSystem(museumManagementSystemDto.getMuseumManagementSystemId());
        List<LoanRequestDto> loanRequestsByStatusDto = new ArrayList<LoanRequestDto>();
        for (LoanRequest loanRequest : loanRequestService.getAllActiveLoanRequests(museumManagementSystem.getSystemId())) {
            if (loanRequest.getStatus().equals(LoanRequest.LoanStatus.Pending) && loanRequest.getMuseumManagementSystem().getSystemId() == museumManagementSystem.getSystemId()
                    && loanRequest.getClient().getCurrentLoanNumber() < 5) {
                loanRequestsByStatusDto.add(ToDtoHelper.convertToDto(loanRequest));
            }
        }
        return loanRequestsByStatusDto;
    }

    /**
     * Gets all loan requests registered by its client
     * @param clientDto
     * @return a list of loan request objects
     */
    @GetMapping(value = { "/loanRequest/{client}", "/loanRequest/{client}/" })
    public List<LoanRequestDto> getAllLoanRequestsByClient(@RequestParam(name = "client") ClientDto clientDto) throws IllegalArgumentException {
        Client client = accountManagementService.getClient(clientDto.getUsername());
        List<LoanRequestDto> loanRequestsByClientDto = new ArrayList<LoanRequestDto>();
        for (LoanRequest loanRequest : loanRequestService.getAllLoanRequestsByClient(client)) {
            if (loanRequest.getClient().equals(client)) {
                loanRequestsByClientDto.add(ToDtoHelper.convertToDto(loanRequest));
            }
        }
        return loanRequestsByClientDto;
    }

    /**
     * Approve loan request
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = { "/loanRequest/approval/{requestId}", "/loanRequest/approval/{requestId}/" })
    public LoanRequestDto approveLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest=loanRequestService.approveLoanRequest(requestId);
        return ToDtoHelper.convertToDto(loanRequest);
    }

    /**
     * Reject loan request
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = { "/loanRequest/rejection/{requestId}", "/loanRequest/rejection/{requestId}/" })
    public LoanRequestDto rejectLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest=loanRequestService.rejectLoanRequest(requestId);
        return ToDtoHelper.convertToDto(loanRequest);
    }

    /**
     * Confirm artifact's return
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = { "/loanRequest/returning/{requestId}", "/loanRequest/returning/{requestId}/" })
    public LoanRequestDto returnLoanedArtifact(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest=loanRequestService.returnLoanedArtifact(requestId);
        return ToDtoHelper.convertToDto(loanRequest);
    }
}
