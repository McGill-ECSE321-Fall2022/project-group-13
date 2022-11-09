package ca.mcgill.ecse321.MMSBackend.service;

import java.sql.Time;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;


public class MuseumManagementSystemService {
    

    @Autowired
    MuseumManagementSystemRepository museumManagementSystemRepository;

    @Autowired
    RoomRepository roomRepository;

    /**
     * Create museum management system
     */
    @Transactional
    public MuseumManagementSystem createMuseumManagementSystem() {
        MuseumManagementSystem  museumManagementSystem = new MuseumManagementSystem();
        museumManagementSystem.setName("Museum Management System Default Name");
        museumManagementSystem.setTicketFee(0.00);
        museumManagementSystem.setOpenTime(Time.valueOf("9:00:00"));
        museumManagementSystem.setCloseTime(Time.valueOf("17:00:00"));
        museumManagementSystem.setMaxLoanNumber(5);
        museumManagementSystemRepository.save(museumManagementSystem);
        return museumManagementSystem;
    }

    /**
     * Get museum management system
     */
    @Transactional
    public MuseumManagementSystem getMuseumManagementSystem(int museumManagementSystemId) {

        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);

        if(museumManagementSystem == null) {
            throw new IllegalArgumentException("Museum Management System does not exist");
        }else{
            return museumManagementSystem;
        }
    }

    /**
     * Delete museum management system
     *
     */
    @Transactional
    public void deleteMuseumManagementSystem(int museumManagementSystemId) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
    
        if(museumManagementSystem == null) {
            throw new IllegalArgumentException("Museum Management System does not exist");
        }else{
            museumManagementSystemRepository.delete(museumManagementSystem);
        }
    }
    
    /**
     * Set museum management system name
     *
     */
    @Transactional
    public void setMuseumManagementSystemName(int museumManagementSystemId, String name) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
        
        if(museumManagementSystem == null) {
            throw new IllegalArgumentException("Museum Management System does not exist");
        }else{
            museumManagementSystem.setName(name);
        }
    }

    /**
     * Get all museum management systems
     *
     */
    @Transactional
    public Iterable<MuseumManagementSystem> getAllMuseumManagementSystems() {
        return museumManagementSystemRepository.findAll();
    }

    /**
     * Set current museum ticket price
     */
    @Transactional
    public void setMuseumTicketPrice(int museumManagementSystemId, double price) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
        
        if(museumManagementSystem == null) {
            throw new IllegalArgumentException("Museum Management System does not exist");
        }else{
            museumManagementSystem.setTicketFee(price);
        }
    }

    /**
     * Get current museum ticket price
     */
    @Transactional
    public void getMuseumTicketPrice(int museumManagementSystemId) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
       
        if(museumManagementSystem == null) {
            throw new IllegalArgumentException("Museum Management System does not exist");
        }else{
            museumManagementSystem.getTicketFee();;
        }
    }

    /**
     * create museum rooms
     */
    @Transactional
    public void createMuseumRooms(int museumManagementSystemId){

        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
        
        //create 5 small rooms
        for(int i = 1; i <= 5; i++){

            Room currentRoom = new Room();
            currentRoom.setName("Small Room " + i);
            currentRoom.setType(RoomType.Small);
            currentRoom.setMuseumManagementSystem(museumManagementSystem);
            roomRepository.save(currentRoom);
        }

        //create 5 large rooms
        for(int i = 1; i <= 5; i++){

            Room currentRoom = new Room();
            currentRoom.setName("Large Room " + i);
            currentRoom.setType(RoomType.Large);
            currentRoom.setMuseumManagementSystem(museumManagementSystem);
            roomRepository.save(currentRoom);
        }

        //create storage room
        Room storageRoom = new Room();
        storageRoom.setName("Storage Room");
        storageRoom.setType(RoomType.Storage);
        storageRoom.setMuseumManagementSystem(museumManagementSystem);
        roomRepository.save(storageRoom);

    }

}
