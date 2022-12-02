package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Room;

import java.util.List;

/**
 * @author Mona Kalaoun (m-kln)
 */

public interface RoomRepository extends CrudRepository<Room, Integer>{
    public Room findRoomByRoomId(Integer roomId);
    public List<Room> findRoomByName(String name);
}