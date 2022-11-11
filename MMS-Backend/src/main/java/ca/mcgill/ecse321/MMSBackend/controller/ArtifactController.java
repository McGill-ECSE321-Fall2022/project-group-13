package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.service.ArtifactService;
import ca.mcgill.ecse321.MMSBackend.controller.ToDtoHelper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ArtifactController {

    @Autowired
    private ArtifactService service;

    @GetMapping(value = { "/artifacts", "/artifacts/" })
    public List<ArtifactDto> getAllArtifacts(){
        return service.getAllArtifacts().stream().map(p -> ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @GetMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public ArtifactDto getArtifactById(@PathVariable("id") Integer id) throws IllegalArgumentException{
        return ToDtoHelper.convertToDto(service.getArtifact(id));
    }

    @GetMapping(value = { "/artifacts/{roomType}", "/artifacts/{roomType}/" })
    public List<ArtifactDto> getArtifactsByRoomType(@PathVariable("roomType") Room.RoomType roomType) throws
            IllegalArgumentException {
        return service.getAllArtifactsByRoomType(roomType).stream().map(p ->
                ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @GetMapping(value = { "/artifacts/{roomId}", "/artifacts/{roomId}/" })
    public List<ArtifactDto> getArtifactsByRoomId(@PathVariable("roomId") int roomId) throws
            IllegalArgumentException {
        return service.getAllArtifactsByRoomId(roomId).stream().map(p ->
                ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @GetMapping(value = { "/artifacts/{status}", "/artifacts/{status}/" })
    public List<ArtifactDto> getArtifactsByLoanStatus(@PathVariable("status") Artifact.LoanStatus status) throws
            IllegalArgumentException {
        return service.getAllArtifactsByLoanStatus(status).stream().map(p ->
                ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @GetMapping(value = { "/artifacts/{state}", "/artifacts/{state}/" })
    public List<ArtifactDto> getArtifactsByState(@PathVariable("state") boolean state) throws
            IllegalArgumentException {
        return service.getAllArtifactsByState(state).stream().map(p ->
                ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @PutMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public ArtifactDto editArtifact(@PathVariable("id") int artifactId, @RequestParam String name, @RequestParam String
            description, @RequestParam String image, @RequestParam Artifact.LoanStatus status, @RequestParam double
            loanFee, @RequestParam boolean isDamaged, @RequestParam double worth, @RequestParam int roomId,
                                    @RequestParam int systemId) throws IllegalArgumentException{

        Artifact artifact = service.editArtifact(artifactId, name, description, image, status, loanFee, isDamaged,
                worth, roomId, systemId);
        return ToDtoHelper.convertToDto(artifact);
    }

    @PostMapping(value = { "/artifacts/{name}", "/artifacts/{name}/" })
    public ArtifactDto createArtifact(@PathVariable("name") String name, @RequestParam String description, @RequestParam
            String image, @RequestParam Artifact.LoanStatus status, @RequestParam double loanFee, @RequestParam boolean
            isDamaged, @RequestParam double worth, @RequestParam int roomId, @RequestParam int systemId) throws
            IllegalArgumentException{
        Artifact artifact = service.createArtifact(name, description, image, status, loanFee, isDamaged, worth, roomId,
                systemId);
        return ToDtoHelper.convertToDto(artifact);
    }

    @DeleteMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public void deleteArtifact(@PathVariable("id") int id) throws IllegalArgumentException {
        service.deleteArtifact(id);
    }







}
