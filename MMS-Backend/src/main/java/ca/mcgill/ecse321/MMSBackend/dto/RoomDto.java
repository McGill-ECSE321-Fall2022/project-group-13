package ca.mcgill.ecse321.MMSBackend.dto;

import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;

/**
 * @author Mona Kalaoun (m-kln)
 */
public class RoomDto {

    //public enum RoomType { Small, Large, Storage }

    private Room.RoomType type;
    private int roomId;
    private String name;
    private MuseumManagementSystemDto museumManagementSystem;

    public RoomDto(){

    }

    public RoomDto(int roomId, String name, Room.RoomType type, MuseumManagementSystemDto museumManagementSystem){
        this.roomId = roomId;
        this.name = name;
        this.type = type;
        this.museumManagementSystem = museumManagementSystem;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public Room.RoomType getType() {
        return type;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }
}
