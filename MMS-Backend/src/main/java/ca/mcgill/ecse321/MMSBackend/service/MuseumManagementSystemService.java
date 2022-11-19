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
     * Create museum management system
     * 
     * @author : Lucy Zhang (Lucy-Zh) and Samantha Perez Hoffman (samperezh)
     * 
     */
    @Transactional
    public MuseumManagementSystem createMuseumManagementSystem() {

        if(museumManagementSystemRepository.count() != 0) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Museum Management System already exists");
        }

        MuseumManagementSystem museumManagementSystem = new MuseumManagementSystem();
        museumManagementSystem.setName("Museum Management System Default Name");
        museumManagementSystem.setTicketFee(0.00);
        museumManagementSystem.setOpenTime(Time.valueOf("9:00:00"));
        museumManagementSystem.setCloseTime(Time.valueOf("17:00:00"));
        museumManagementSystem.setMaxLoanNumber(5);

        museumManagementSystem = museumManagementSystemRepository.save(museumManagementSystem);

        //create 7 days of the week 
        createMuseumSpecificWeekDays(museumManagementSystem);
        //create rooms 
        createMuseumRooms(museumManagementSystem);

        return museumManagementSystem;
    }

    /**
     * Get museum management system through id
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     * @param museumManagementSystemId
     * 
     */
    @Transactional
    public MuseumManagementSystem getMuseumManagementSystem(int museumManagementSystemId) {

        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository
                .findMuseumManagementSystemBySystemId(museumManagementSystemId);

        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Museum Management System does not exist");
        } else {
            return museumManagementSystem;
        }
    }

    /**
     * Delete museum management system through id
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     * @param museumManagementSystemId
     *
     */
    @Transactional
    public void deleteMuseumManagementSystem(int museumManagementSystemId) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository
                .findMuseumManagementSystemBySystemId(museumManagementSystemId);

        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Museum Management System does not exist");
        } else {
            museumManagementSystemRepository.delete(museumManagementSystem);
        }
    }

    /**
     * Set museum management system name through id
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     * @param museumManagementSystemId, name
     *
     */
    @Transactional
    public void setMuseumManagementSystemName(int museumManagementSystemId, String name) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository
                .findMuseumManagementSystemBySystemId(museumManagementSystemId);

        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Museum Management System does not exist");
        } else {
            museumManagementSystem.setName(name);
        }
    }

    /**
     * Get museum management system
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     *
     */
    @Transactional
    public List<MuseumManagementSystem> getAllMuseumManagementSystem() {
        return toList(museumManagementSystemRepository.findAll());
    }

    /**
     * Set current museum ticket price through id
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     * @param museumManagementSystemId, price
     * 
     */
    @Transactional
    public void setMuseumTicketPrice(int museumManagementSystemId, double price) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository
                .findMuseumManagementSystemBySystemId(museumManagementSystemId);

        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Museum Management System does not exist");
        } else if(price < 0){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Ticket price is not valid");
        }else {
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

        List<Room> roomsByType = new ArrayList<Room>();
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
     * @param museumManagementSystemId
     */
    @Transactional
    public int getMaxLoanNumberOfMms(int museumManagementSystemId) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
        return museumManagementSystem.getMaxLoanNumber();
    }

    /**
     * Set museum management system's max loan number for clients
     *
     * @author : Nazia Chowdhury (naziaC)
     * @param museumManagementSystemId
     */
    @Transactional
    public void setMaxLoanNumberOfMms(int museumManagementSystemId) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository
                .findMuseumManagementSystemBySystemId(museumManagementSystemId);
        if (museumManagementSystem == null) {
            throw new IllegalArgumentException("Museum Management System does not exist");
        } else {
            museumManagementSystem.setMaxLoanNumber(5);
            museumManagementSystemRepository.save(museumManagementSystem);
        }
    }

    /**
     * Set museum management system opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param newOpenTime
     * @param newCloseTime
     * @param museumManagementSystemId
     */
    @Transactional
    public MuseumManagementSystem setOpeningHours(Time newOpenTime, Time newCloseTime, int museumManagementSystemId) {
        if(newOpenTime == null || newCloseTime == null) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Opening and closing times cannot be null.");
        }
        if (newOpenTime.after(newCloseTime)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Opening time cannot be after closing time.");
        }
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository
                .findMuseumManagementSystemBySystemId(museumManagementSystemId);
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
     * @param museumManagementSystemId
     */
    @Transactional
    public List<Time> getOpeningHours (int museumManagementSystemId) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository
                .findMuseumManagementSystemBySystemId(museumManagementSystemId);
        if (museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Museum Management System does not exist.");
        }
        List<Time> openingHours = new ArrayList<Time>();
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
     * Helper method that creates the 7 specific week days of the museum management system
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param museumManagementSystem
     */
    private void createMuseumSpecificWeekDays(MuseumManagementSystem museumManagementSystem){
        SpecificWeekDay monday = new SpecificWeekDay();
        monday.setDayType(DayType.Monday);
        monday.setIsClosed(false);
        monday.setMuseumManagementSystem(museumManagementSystem);

        SpecificWeekDay tuesday = new SpecificWeekDay();
        tuesday.setDayType(DayType.Tuesday);
        tuesday.setIsClosed(false);
        tuesday.setMuseumManagementSystem(museumManagementSystem);

        SpecificWeekDay wednesday = new SpecificWeekDay();
        wednesday.setDayType(DayType.Wednesday);
        wednesday.setIsClosed(false);
        wednesday.setMuseumManagementSystem(museumManagementSystem);

        SpecificWeekDay thursday = new SpecificWeekDay();
        thursday.setDayType(DayType.Thursday);
        thursday.setIsClosed(false);
        thursday.setMuseumManagementSystem(museumManagementSystem);

        SpecificWeekDay friday = new SpecificWeekDay();
        friday.setDayType(DayType.Friday);
        friday.setIsClosed(false);
        friday.setMuseumManagementSystem(museumManagementSystem);
        
        SpecificWeekDay saturday = new SpecificWeekDay();
        saturday.setDayType(DayType.Saturday);
        saturday.setIsClosed(false);
        saturday.setMuseumManagementSystem(museumManagementSystem);

        SpecificWeekDay sunday = new SpecificWeekDay();
        sunday.setDayType(DayType.Sunday);
        sunday.setIsClosed(false);
        sunday.setMuseumManagementSystem(museumManagementSystem);

        specificWeekDayRepository.save(monday);
        specificWeekDayRepository.save(tuesday);
        specificWeekDayRepository.save(wednesday);
        specificWeekDayRepository.save(thursday);
        specificWeekDayRepository.save(friday);
        specificWeekDayRepository.save(saturday);
        specificWeekDayRepository.save(sunday);
    }

    /**
     * helper method that creates the rooms of the museum management system
     * 
     * @author : Lucy Zhang (Lucy-Zh)
     * @param museumManagementSystem
     *
     */
    public void createMuseumRooms(MuseumManagementSystem museumManagementSystem) {
        // create 5 small rooms
        for (int i = 1; i <= 5; i++) {

            Room currentRoom = new Room();
            currentRoom.setName("Small Room " + i);
            currentRoom.setType(RoomType.Small);
            currentRoom.setMuseumManagementSystem(museumManagementSystem);
            roomRepository.save(currentRoom);
        }

        // create 5 large rooms
        for (int i = 1; i <= 5; i++) {

            Room currentRoom = new Room();
            currentRoom.setName("Large Room " + i);
            currentRoom.setType(RoomType.Large);
            currentRoom.setMuseumManagementSystem(museumManagementSystem);
            roomRepository.save(currentRoom);
        }

        // create storage room
        Room storageRoom = new Room();
        storageRoom.setName("Storage Room");
        storageRoom.setType(RoomType.Storage);
        storageRoom.setMuseumManagementSystem(museumManagementSystem);
        roomRepository.save(storageRoom);
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
