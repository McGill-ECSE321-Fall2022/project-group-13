package ca.mcgill.ecse321.MMSBackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;

import java.sql.Time;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoomRepositoryTests {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @AfterEach
    public void clearDatabase() {
        roomRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadRoom(){

        MuseumManagementSystem mms = new MuseumManagementSystem();

        String name = "MK's MMS";
        Time openTime = Time.valueOf("9:00:00");
        Time closeTime = Time.valueOf("17:00:00");
        int maxLoanNumber = 5;
        double ticketFee = 13.25;

        mms.setName(name);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);

        mmsRepository.save(mms);

        //Create a room
        Room room = new Room();

        RoomType type = RoomType.Small;
        room.setType(type);

        String roomName = "Small Room";
        room.setName(roomName);

        room.setMuseumManagementSystem(mms);

        room = roomRepository.save(room);
        int id = room.getRoomId();

        Integer mmsId = mms.getSystemId();

        mms = null;
        room = null;

        room = roomRepository.findRoomByRoomId(id);

        assertNotNull(room);
        assertEquals(roomName, room.getName());
        assertEquals(type, room.getType());
        assertEquals(id, room.getRoomId());

        assertNotNull(room.getMuseumManagementSystem());
        assertEquals(mmsId, room.getMuseumManagementSystem().getSystemId());
        assertEquals(name, room.getMuseumManagementSystem().getName());
        assertEquals(openTime, room.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, room.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, room.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, room.getMuseumManagementSystem().getTicketFee());

    }



}
