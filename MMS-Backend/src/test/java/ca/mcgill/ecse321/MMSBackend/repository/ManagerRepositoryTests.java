package ca.mcgill.ecse321.MMSBackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.model.Manager;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

/**
 * @author Nikolas Pasichnik (NikolasPasichnik)
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ManagerRepositoryTests {

    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private SpecificWeekDayRepository specificWeekDayRepository;

    @BeforeEach
    public void clearDatabase() {
        // Delete the manager first to avoid violating not-null constraints
        managerRepository.deleteAll();
        artifactRepository.deleteAll();
        roomRepository.deleteAll();
        specificWeekDayRepository.deleteAll();
        employeeRepository.deleteAll();
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @AfterEach
    public void clearDatabaseAfter() {
        // Delete the manager first to avoid violating not-null constraints
        managerRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testManagerRepository() {
        // Creating a museum management system using a plain constructor
        MuseumManagementSystem mms = new MuseumManagementSystem();
        String museumName = "Manager's MMS";
        Time openTime = Time.valueOf("9:00:00");
        Time closeTime = Time.valueOf("17:00:00");
        int maxLoanNumber = 5;
        double ticketFee = 13.25;

        // Setting the museum management system's attributes using setters
        mms.setName(museumName);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);

        // Saving the museum anagement system to the db
        mmsRepository.save(mms);

        // Creating a manager
        Manager manager = new Manager();
        String username = "MarwanManager";
        String name = "Marwan Kanaan";
        String password = "ecse321";

        manager.setUsername(username);
        manager.setName(name);
        manager.setPassword(password);
        manager.setMuseumManagementSystem(mms);

        // Saving the manager to the database
        managerRepository.save(manager);

        // Getting ids of Manager and mms
        String managerUsername = manager.getUsername();
        int museumID = mms.getSystemId();

        // Make the variables null
        mms = null;
        manager = null;

        // Fetching information from the database
        manager = managerRepository.findManagerByUsername(managerUsername);

        // Checking for existence of manager and valid username
        assertNotNull(manager);
        assertEquals(managerUsername, manager.getUsername());

        // Checking for proper connection between museum and manager
        assertNotNull(manager.getMuseumManagementSystem());
        assertEquals(museumID, manager.getMuseumManagementSystem().getSystemId());
        assertEquals(museumName, manager.getMuseumManagementSystem().getName());
        assertEquals(openTime, manager.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, manager.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, manager.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, manager.getMuseumManagementSystem().getTicketFee());

    }
}
