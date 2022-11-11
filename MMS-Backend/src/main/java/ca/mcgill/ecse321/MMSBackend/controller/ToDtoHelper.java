package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.dto.*;

public class ToDtoHelper {

    //to be completed
    public MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms){
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        MuseumManagementSystemDto system = new MuseumManagementSystemDto();
        return system;
    }

    /**
     * @author Mona Kalaoun (m-kln)
     * @param r
     * @return
     */
    public RoomDto convertToDto(Room r){
        if (r == null) {
            throw new IllegalArgumentException("There is no such Room!");
        }
        MuseumManagementSystemDto systemDto = convertToDto(r.getMuseumManagementSystem());
        RoomDto room = new RoomDto(r.getName(), r.getType(), systemDto);
        return room;
    }

    /**
     * @author Mona Kalaoun (m-kln)
     * @param a
     * @return
     */
    public ArtifactDto convertToDto(Artifact a) {
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
