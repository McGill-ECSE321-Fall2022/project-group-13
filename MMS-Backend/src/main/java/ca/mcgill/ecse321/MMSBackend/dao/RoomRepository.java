package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Room;

//Mona

public interface RoomRepository extends CrudRepository<Room, String>{
    public Room findRoomByRoomId(String roomId);
}