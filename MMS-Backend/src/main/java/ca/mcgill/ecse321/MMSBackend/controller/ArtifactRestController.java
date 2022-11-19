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
 * dont trust my work so far
 */
@CrossOrigin(origins = "*")
@RestController
public class ArtifactRestController {

    @Autowired
    private ArtifactService service;

    @GetMapping(value = { "/artifacts", "/artifacts/" })
    public ResponseEntity<List<ArtifactDto>>  getAllArtifacts(){
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifacts().stream().map(ToDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public ResponseEntity<ArtifactDto> getArtifactById(@PathVariable("id") Integer id) throws IllegalArgumentException{
        return new ResponseEntity<ArtifactDto> (ToDtoHelper.convertToDto(service.getArtifact(id)), HttpStatus.OK);
    }

    @GetMapping(value = { "/artifacts/{roomType}", "/artifacts/{roomType}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByRoomType(@PathVariable("roomType") Room.RoomType roomType) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByRoomType(roomType).stream().map(ToDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = { "/artifacts/{roomId}", "/artifacts/{roomId}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByRoomId(@PathVariable("roomId") int roomId) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByRoomId(roomId).stream().map(ToDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = { "/artifacts/{status}", "/artifacts/{status}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByLoanStatus(@PathVariable("status") Artifact.LoanStatus status) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByLoanStatus(status).stream().map(ToDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = { "/artifacts/{state}", "/artifacts/{state}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByState(@PathVariable("state") boolean state) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByState(state).stream().map(ToDtoHelper::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public ResponseEntity<ArtifactDto> editArtifact(@PathVariable("id") int artifactId, @RequestParam String name, @RequestParam String
            description, @RequestParam String image, @RequestParam Artifact.LoanStatus status, @RequestParam double
            loanFee, @RequestParam boolean isDamaged, @RequestParam double worth, @RequestParam int roomId,
                                    @RequestParam int systemId) throws IllegalArgumentException{

        Artifact artifact = service.editArtifact(artifactId, name, description, image, status, loanFee, isDamaged,
                worth, roomId, systemId);
        return new ResponseEntity<ArtifactDto> (ToDtoHelper.convertToDto(artifact), HttpStatus.OK);
    }

    @PostMapping(value = { "/artifacts/{name}", "/artifacts/{name}/" })
    public ResponseEntity<ArtifactDto> createArtifact(@PathVariable("name") String name, @RequestParam String description, @RequestParam
            String image, @RequestParam Artifact.LoanStatus status, @RequestParam double loanFee, @RequestParam boolean
            isDamaged, @RequestParam double worth, @RequestParam int roomId, @RequestParam int systemId) throws
            IllegalArgumentException{
        Artifact artifact = service.createArtifact(name, description, image, status, loanFee, isDamaged, worth, roomId,
                systemId);
        return new ResponseEntity<ArtifactDto> (ToDtoHelper.convertToDto(artifact), HttpStatus.OK);
    }

    @DeleteMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public void deleteArtifact(@PathVariable("id") int id) throws IllegalArgumentException {
        service.deleteArtifact(id);
    }
}
