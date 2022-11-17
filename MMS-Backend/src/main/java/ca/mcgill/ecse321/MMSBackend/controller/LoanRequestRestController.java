package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.dto.ArtifactDto;
import ca.mcgill.ecse321.MMSBackend.dto.ClientDto;
import ca.mcgill.ecse321.MMSBackend.dto.LoanRequestDto;
import ca.mcgill.ecse321.MMSBackend.dto.MuseumManagementSystemDto;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.LoanRequest;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.service.ArtifactService;
import ca.mcgill.ecse321.MMSBackend.service.ClientAccountService;
import ca.mcgill.ecse321.MMSBackend.service.LoanRequestService;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;
import org.springframework.beans.factory.annotation.Autowired;
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
     */
    @PostMapping(value = {"/loanRequest", "/loanRequest/"})
    public LoanRequestDto createLoanRequest(@RequestParam(name = "loanDuration") int loanDuration, @RequestParam(name = "artifact") ArtifactDto artifactDto,
                                            @RequestParam(name = "client") ClientDto clientDto, @RequestParam(name = "mmsSystem") MuseumManagementSystemDto museumManagementSystemDto) throws IllegalArgumentException {

        Artifact artifact = artifactService.getArtifact(artifactDto.getArtifactId());
        Client client = clientAccountService.getClient(clientDto.getUsername());
        MuseumManagementSystem museumManagementSystem = mmsService.getMuseumManagementSystem(museumManagementSystemDto.getMuseumManagementSystemId());
        LoanRequest loanRequest = loanRequestService.createLoanRequest(loanDuration, artifact, client, museumManagementSystem);

        return ToDtoHelper.convertToDto(loanRequest);
    }

    /**
     * Gets a loan request by its id
     *
     * @param requestId
     * @return loan request object
     */
    @GetMapping(value = {"/loanRequest/{id}", "/loanRequest/{id}/"})
    public LoanRequestDto getLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.getLoanRequest(requestId);
        return ToDtoHelper.convertToDto(loanRequest);
    }

    @GetMapping(value = {"/loanRequests", "/loanRequests/"})
    public List<LoanRequestDto> getAllLoanRequests() {
        return loanRequestService.getAllLoanRequests().stream().map(ToDtoHelper::convertToDto).collect(Collectors.toList());
    }

    /**
     * Gets all loan requests registered by its status
     *
     * @param statusDto
     * @return the list of loan request dtos with the specified status
     * @throws IllegalArgumentException
     */
    @GetMapping(value = {"/loanRequests/{status}", "/loanRequests/{status}/"})
    public List<LoanRequestDto> getAllLoanRequestsByStatus(@PathVariable("status") LoanRequestDto.LoanRequestStatusDto statusDto) throws IllegalArgumentException {
        List<LoanRequestDto> loanRequestsDtos = new ArrayList<>();

        for (LoanRequest loanRequest : loanRequestService.getAllLoanRequestsByStatus(ToDtoHelper.convertToDomainObject(statusDto))) {
            loanRequestsDtos.add(ToDtoHelper.convertToDto(loanRequest));
        }

        return loanRequestsDtos;
    }

    /**
     * Gets all loan requests than can be approved/rejected
     *
     * @return a list of loan request objects for a specific client than can be approved/rejected
     */
    @GetMapping(value = {"/activeLoanRequests", "/activeLoanRequests/"})
    public List<LoanRequestDto> getAllActiveLoanRequests() throws IllegalArgumentException {
        List<LoanRequestDto> loanRequestsByStatusDto = new ArrayList<LoanRequestDto>();
        for (LoanRequest loanRequest : loanRequestService.getAllActiveLoanRequests()) {
            loanRequestsByStatusDto.add(ToDtoHelper.convertToDto(loanRequest));
        }
        return loanRequestsByStatusDto;
    }

    /**
     * Gets all loan requests registered by its client
     *
     * @param clientDto
     * @return a list of loan request objects
     */
    @GetMapping(value = {"/loanRequests/{client}", "/loanRequests/{client}/"})
    public List<LoanRequestDto> getAllLoanRequestsByClient(@RequestParam(name = "client") ClientDto clientDto) throws IllegalArgumentException {
        Client client = clientAccountService.getClient(clientDto.getUsername());
        List<LoanRequestDto> loanRequestsByClientDto = new ArrayList<LoanRequestDto>();
        for (LoanRequest loanRequest : loanRequestService.getAllLoanRequestsByClient(client)) {
            loanRequestsByClientDto.add(ToDtoHelper.convertToDto(loanRequest));
        }
        return loanRequestsByClientDto;
    }

    /**
     * Approve loan request
     *
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = {"/loanRequest/approveRequest/{requestId}", "/loanRequest/approveRequest/{requestId}/"})
    public LoanRequestDto approveLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.approveLoanRequest(requestId);
        return ToDtoHelper.convertToDto(loanRequest);
    }

    /**
     * Reject loan request
     *
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = {"/loanRequest/rejectRequest/{requestId}", "/loanRequest/rejectRequest/{requestId}/"})
    public LoanRequestDto rejectLoanRequest(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.rejectLoanRequest(requestId);
        return ToDtoHelper.convertToDto(loanRequest);
    }

    /**
     * Confirm artifact's return
     *
     * @param requestId
     * @return loan request object
     */
    @PutMapping(value = {"/loanRequest/loanReturn/{requestId}", "/loanRequest/loanReturn/{requestId}/"})
    public LoanRequestDto returnLoanedArtifact(@PathVariable("requestId") int requestId) throws IllegalArgumentException {
        LoanRequest loanRequest = loanRequestService.returnLoanedArtifact(requestId);
        return ToDtoHelper.convertToDto(loanRequest);
    }
}
