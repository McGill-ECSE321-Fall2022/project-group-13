package ca.mcgill.ecse321.MMSBackend.service;

import java.sql.Time;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;


public class MuseumManagementSystemService {
    

    @Autowired
    MuseumManagementSystemRepository museumManagementSystemRepository;

    /**
     * Create museum management system
     */
    @Transactional
    public MuseumManagementSystem createMuseumManagementSystem() {
        MuseumManagementSystem  museumManagementSystem = new MuseumManagementSystem();
        museumManagementSystem.setSystemId(museumManagementSystem.getSystemId());
        museumManagementSystem.setName("Museum Management System Default Name");
        museumManagementSystem.setTicketFee(0.00);
        museumManagementSystem.setOpenTime(Time.valueOf("9:00:00"));
        museumManagementSystem.setCloseTime(Time.valueOf("17:00:00"));
        museumManagementSystem.setMaxLoanNumber(0);
        museumManagementSystemRepository.save(museumManagementSystem);
        return museumManagementSystem;
    }

    /**
     * Get museum management system
     */
    @Transactional
    public MuseumManagementSystem getMuseumManagementSystem(int museumManagementSystemId) {
        return museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
    }

    /**
     * Delete museum management system
     *
     */
    @Transactional
    public void deleteMuseumManagementSystem(int museumManagementSystemId) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
        museumManagementSystemRepository.delete(museumManagementSystem);
    }
    
    /**
     * Set museum management system name
     *
     */
    @Transactional
    public void setMuseumManagementSystemName(int museumManagementSystemId, String name) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
        museumManagementSystem.setName(name);
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
        museumManagementSystem.setTicketFee(price);
    }

    /**
     * Get current museum ticket price
     */
    @Transactional
    public void getMuseumTicketPrice(int museumManagementSystemId) {
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumManagementSystemId);
        museumManagementSystem.getTicketFee();
    }
}
