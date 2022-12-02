package ca.mcgill.ecse321.MMSBackend.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
import ca.mcgill.ecse321.MMSBackend.dao.SpecificWeekDayRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;

@Service
public class MuseumManagementSystemService {

    @Autowired
    MuseumManagementSystemRepository museumManagementSystemRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    SpecificWeekDayRepository specificWeekDayRepository;

    /**
     * Get museum management system 
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     * 
     */
    @Transactional
    public MuseumManagementSystem getMuseumManagementSystem() {

        MuseumManagementSystem museumManagementSystem = toList(museumManagementSystemRepository.findAll()).get(0);

        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Museum Management System does not exist");
        } else {
            return museumManagementSystem;
        }
    }

    /**
     * Set museum management system name through id
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     * @param name
     *
     */
    @Transactional
    public void setMuseumManagementSystemName(String name) {
        MuseumManagementSystem museumManagementSystem = toList(museumManagementSystemRepository.findAll()).get(0);

        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Museum Management System does not exist");
        } else {
            museumManagementSystem.setName(name);
        }
    }

    /**
     * Set current museum ticket price through id
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     * @param  price
     * 
     */
    @Transactional
    public void setMuseumTicketPrice(double price) {
        MuseumManagementSystem museumManagementSystem = toList(museumManagementSystemRepository.findAll()).get(0);

        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Museum Management System does not exist");
        } else if(price < 0){
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Ticket price is not valid");
        } else {
            museumManagementSystem.setTicketFee(price);
        }
    }

    /**
     * Gets a single room by its id to store/display the donated artifact
     * 
     * @author Yu An Lu (yu-an-lu)
     * @param roomId
     * @return room object
     * 
     */
    @Transactional
    public Room getRoom(int roomId) {
        if (roomRepository.findRoomByRoomId(roomId) == null)
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Room does not exist");
        
        return roomRepository.findRoomByRoomId(roomId);
    }

    /**
     * Gets all rooms registered in the museum
     * 
     * @author Yu An Lu (yu-an-lu)
     * @return a list of room objects
     * 
     */
    @Transactional
    public List<Room> getAllRooms() {
        return toList(roomRepository.findAll());
    }

    /**
     * Gets all rooms of a specific type
     * 
     * @author Yu An Lu (yu-an-lu)
     * @param roomType
     * @return a list of room objects
     * 
     */
    @Transactional
    public List<Room> getAllRoomsByType(RoomType roomType) {
        if (roomType == null) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Room type cannot be null");
        }

        List<Room> roomsByType = new ArrayList<>();
        for (Room room : roomRepository.findAll()) {
            if (room.getType().equals(roomType)) {
                roomsByType.add(room);
            }
        }
        return roomsByType;
    }

    /**
     * get museum management system's max loan number for clients
     *
     * @author : Nazia Chowdhury (naziaC)
     */
    @Transactional
    public int getMaxLoanNumberOfMms() {
        MuseumManagementSystem museumManagementSystem = toList(museumManagementSystemRepository.findAll()).get(0);
        return museumManagementSystem.getMaxLoanNumber();
    }

    /**
     * Set museum management system's max loan number for clients
     *
     * @author : Nazia Chowdhury (naziaC)
     */
    @Transactional
    public void setMaxLoanNumberOfMms(int maxLoanNumber) {
        MuseumManagementSystem museumManagementSystem = toList(museumManagementSystemRepository.findAll()).get(0);
        if(maxLoanNumber <= 0){
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Maximum loan number is not valid");
        } else {
            museumManagementSystem.setMaxLoanNumber(maxLoanNumber);
            museumManagementSystemRepository.save(museumManagementSystem);
        }
    }

    /**
     * Set museum management system opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param newOpenTime
     * @param newCloseTime
     */
    @Transactional
    public MuseumManagementSystem setOpeningHours(Time newOpenTime, Time newCloseTime) {
        if(newOpenTime == null || newCloseTime == null) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Opening and closing times cannot be null.");
        }
        if (newOpenTime.after(newCloseTime)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Opening time cannot be after closing time.");
        }
        MuseumManagementSystem museumManagementSystem = toList(museumManagementSystemRepository.findAll()).get(0);
        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Museum Management System does not exist.");
        }
        museumManagementSystem.setOpenTime(newOpenTime);
        museumManagementSystem.setCloseTime(newCloseTime);
        museumManagementSystem = museumManagementSystemRepository.save(museumManagementSystem);
        return museumManagementSystem;

    }

    /**
     * Get museum management system opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     */
    @Transactional
    public List<Time> getOpeningHours () {
        MuseumManagementSystem museumManagementSystem = toList(museumManagementSystemRepository.findAll()).get(0);
        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Museum Management System does not exist.");
        }
        List<Time> openingHours = new ArrayList<>();
        openingHours.add(museumManagementSystem.getOpenTime());
        openingHours.add(museumManagementSystem.getCloseTime());
        return openingHours;
    }

    /**
     * Get specificWeekDay by dayType
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param dayType
     */
    @Transactional
    public SpecificWeekDay getSpecificWeekDayByDayType(DayType dayType) {
        SpecificWeekDay specificWeekDay = specificWeekDayRepository.findSpecificWeekDayByDayType(dayType);
        System.out.println("specificWeekDay: " + specificWeekDay);
        if(specificWeekDay == null) {
            System.out.println("specificWeekDay is null");
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Specific Week Day does not exist.");
        }
        return specificWeekDay;
    }

    /**
     * toList helper method (@author eventRegistration authors)
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}
