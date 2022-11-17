package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * @author Mona Kalaoun (m-kln)
 */
public class RoomDto {

    public enum RoomTypeDto { Small, Large, Storage }

    private RoomTypeDto type;
    private int roomId;
    private String name;
    private MuseumManagementSystemDto museumManagementSystem;

    public RoomDto(){

    }

    public RoomDto(int roomId, String name, RoomTypeDto type, MuseumManagementSystemDto museumManagementSystem){
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

    public RoomTypeDto getType() {
        return type;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }
}
