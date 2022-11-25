package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.service.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mona Kalaoun (m-kln)
 *
 * The ArtifactRestController class is responsible for exposing
 * the business logic declared in ArtifactService using a REST API.
 */
@CrossOrigin(origins = "*")
@RestController
public class ArtifactRestController {

    @Autowired
    private ArtifactService service;

    @Autowired
    private ToDtoHelper toDtoHelper;

    /**
     * Get all artifacts
     * @return a list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts", "/artifacts/" })
    public ResponseEntity<List<ArtifactDto>>  getAllArtifacts(){
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifacts().stream().map(toDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get an artifact by its id
     * @param id
     * @return an artifactDto object
     */
    @GetMapping(value = { "/artifacts/getById", "/artifacts/getById/" })
    public ResponseEntity<ArtifactDto> getArtifactById(@RequestParam Integer id) throws IllegalArgumentException{
        return new ResponseEntity<ArtifactDto> (toDtoHelper.convertToDto(service.getArtifact(id)), HttpStatus.OK);
    }

    /**
     * Get all artifacts with the same room type
     * @param roomType
     * @return list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts/{roomType}", "/artifacts/{roomType}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByRoomType(@PathVariable("roomType") Room.RoomType roomType) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByRoomType(roomType).stream().map(toDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all artifacts in the same room
     * @param roomId
     * @return a list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts/getArtifactByRoom/{roomId}", "/artifacts/getArtifactByRoom/{roomId}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByRoomId(@PathVariable("roomId") int roomId) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByRoomId(roomId).stream().map(toDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all artifacts with the same loan status
     * @param status
     * @return a list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts/getByStatus", "/artifacts/getByStatus/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByLoanStatus(@RequestParam String status) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByLoanStatus(toDtoHelper.convertArtifactStringToLoanStatus(status)).stream().map(toDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all artifacts with the same state
     * @param state
     * @return a list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts/getByState/{state}", "/artifacts/{state}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByState(@PathVariable("state") boolean state) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByState(state).stream().map(toDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Edit an artifact with updated information
     *
     * @param artifactId
     * @param name
     * @param description
     * @param image
     * @param status
     * @param loanFee
     * @param isDamaged
     * @param worth
     * @param roomId
     * @param systemId
     * @return an artifactDto object
     */
    @PutMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public ResponseEntity<ArtifactDto> editArtifact(@PathVariable("id") int artifactId, @RequestParam String name, @RequestParam String
            description, @RequestParam String image, @RequestParam Artifact.LoanStatus status, @RequestParam double
            loanFee, @RequestParam boolean isDamaged, @RequestParam double worth, @RequestParam int roomId,
                                    @RequestParam int systemId) throws IllegalArgumentException{

        Artifact artifact = service.editArtifact(artifactId, name, description, image, status, loanFee, isDamaged,
                worth, roomId, systemId);
        return new ResponseEntity<ArtifactDto> (toDtoHelper.convertToDto(artifact), HttpStatus.OK);
    }

    /**
     * Create an artifact
     *
     * @param name
     * @param description
     * @param image
     * @param status
     * @param loanFee
     * @param isDamaged
     * @param worth
     * @param roomId
     * @param systemId
     * @return an artifactDto object
     */
    @PostMapping(value = { "/artifact/createArtifact", "/artifact/createArtifact/" })
    public ResponseEntity<ArtifactDto> createArtifact(@RequestParam String name, @RequestParam String description, @RequestParam
            String image, @RequestParam Artifact.LoanStatus status, @RequestParam double loanFee, @RequestParam boolean
            isDamaged, @RequestParam double worth, @RequestParam int roomId, @RequestParam int systemId) throws
            IllegalArgumentException{
        Artifact artifact = service.createArtifact(name, description, image, status, loanFee, isDamaged, worth, roomId,
                systemId);
        return new ResponseEntity<> (toDtoHelper.convertToDto(artifact), HttpStatus.OK);
    }

    /**
     * Delete an artifact by its id
     * @param id
     */
    @DeleteMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public void deleteArtifact(@PathVariable("id") int id) throws IllegalArgumentException {
        service.deleteArtifact(id);
    }
}
