package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.service.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping
    public List<ArtifactDto> getAllArtifacts(){
        return service.getAllArtifacts().stream().map(p -> convertToDto(p)).collect(Collectors.toList());
    }

    private MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms){
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        MuseumManagementSystemDto system = new MuseumManagementSystemDto();
        return system;
    }

    private RoomDto convertToDto(Room r){
        if (r == null) {
            throw new IllegalArgumentException("There is no such Room!");
        }
        MuseumManagementSystemDto systemDto = convertToDto(r.getMuseumManagementSystem());
        RoomDto room = new RoomDto(r.getName(), r.getType(), systemDto);
        return room;
    }

    private ArtifactDto convertToDto(Artifact a) {
        if (a == null) {
            throw new IllegalArgumentException("There is no such Artifact!");
        }
        RoomDto roomDto = convertToDto(a.getRoomLocation());
        MuseumManagementSystemDto mmsDto = convertToDto(a.getMuseumManagementSystem());

        ArtifactDto artifactDto = new ArtifactDto(a.getName(),a.getImage(),a.getDescription(),a.getLoanStatus(),
                a.getIsDamaged(),a.getLoanFee(),a.getWorth(),roomDto, mmsDto);

        return artifactDto;
    }
}
