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

    /**
     * Get all artifacts
     * @return a list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts", "/artifacts/" })
    public ResponseEntity<List<ArtifactDto>>  getAllArtifacts(){
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifacts().stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get an artifact by its id
     * @param id
     * @return an artifactDto object
     */
    @GetMapping(value = { "/artifacts/getById", "/artifacts/getById/" })
    public ResponseEntity<ArtifactDto> getArtifactById(@RequestParam Integer id) throws IllegalArgumentException{
        return new ResponseEntity<ArtifactDto> (convertToDto(service.getArtifact(id)), HttpStatus.OK);
    }

    /**
     * Get all artifacts with the same room type
     * @param roomType
     * @return list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts/{roomType}", "/artifacts/{roomType}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByRoomType(@PathVariable("roomType") Room.RoomType roomType) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByRoomType(roomType).stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all artifacts in the same room
     * @param roomId
     * @return a list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts/getArtifactByRoom/{roomId}", "/artifacts/getArtifactByRoom/{roomId}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByRoomId(@PathVariable("roomId") int roomId) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByRoomId(roomId).stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all artifacts with the same loan status
     * @param status
     * @return a list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts/getByStatus", "/artifacts/getByStatus/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByLoanStatus(@RequestParam String status) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByLoanStatus(convertArtifactStringToLoanStatus(status)).stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all artifacts with the same state
     * @param state
     * @return a list of artifactDto objects
     */
    @GetMapping(value = { "/artifacts/getByState/{state}", "/artifacts/{state}/" })
    public ResponseEntity<List<ArtifactDto>> getArtifactsByState(@PathVariable("state") boolean state) throws
            IllegalArgumentException {
        return new ResponseEntity<List<ArtifactDto>> (service.getAllArtifactsByState(state).stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
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
     * @return an artifactDto object
     */
    @PutMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public ResponseEntity<ArtifactDto> editArtifact(@PathVariable("id") int artifactId, @RequestParam String name, @RequestParam String
            description, @RequestParam String image, @RequestParam Artifact.LoanStatus status, @RequestParam double
            loanFee, @RequestParam boolean isDamaged, @RequestParam double worth, @RequestParam int roomId) throws IllegalArgumentException{

        Artifact artifact = service.editArtifact(artifactId, name, description, image, status, loanFee, isDamaged,
                worth, roomId);
        return new ResponseEntity<ArtifactDto> (convertToDto(artifact), HttpStatus.OK);
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
     * @return an artifactDto object
     */
    @PostMapping(value = { "/artifact/createArtifact", "/artifact/createArtifact/" })
    public ResponseEntity<ArtifactDto> createArtifact(@RequestParam String name, @RequestParam String description, @RequestParam
            String image, @RequestParam Artifact.LoanStatus status, @RequestParam double loanFee, @RequestParam boolean
            isDamaged, @RequestParam double worth, @RequestParam int roomId) throws
            IllegalArgumentException{
        Artifact artifact = service.createArtifact(name, description, image, status, loanFee, isDamaged, worth, roomId);
        return new ResponseEntity<> (convertToDto(artifact), HttpStatus.OK);
    }

    /**
     * Delete an artifact by its id
     * @param id
     */
    @DeleteMapping(value = { "/artifacts/{id}", "/artifacts/{id}/" })
    public void deleteArtifact(@PathVariable("id") int id) throws IllegalArgumentException {
        service.deleteArtifact(id);
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

    private MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms) {
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        return new MuseumManagementSystemDto(mms.getSystemId(), mms.getName(), mms.getOpenTime(), mms.getCloseTime(), mms.getMaxLoanNumber(), mms.getTicketFee());
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
