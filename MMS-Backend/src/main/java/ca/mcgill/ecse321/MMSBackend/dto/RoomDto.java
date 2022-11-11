package ca.mcgill.ecse321.MMSBackend.dto;

import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;

/**
 * @author Mona Kalaoun (m-kln)
 */
public class RoomDto {

    public enum RoomType { Small, Large, Storage }

    private RoomType type;
    private String name;
    private MuseumManagementSystemDto museumManagementSystem;

    public RoomDto(){

    }

    public RoomDto(String name, RoomType type, MuseumManagementSystemDto museumManagementSystem){
        this.name = name;
        this.type = type;
        this.museumManagementSystem = museumManagementSystem;
    }

    public String getName() {
        return name;
    }

    public RoomType getType() {
        return type;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }
}
