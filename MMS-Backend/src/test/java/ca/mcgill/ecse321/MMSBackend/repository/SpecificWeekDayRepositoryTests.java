
package ca.mcgill.ecse321.MMSBackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;

import java.sql.Time;

/**
 * @author Samantha Perez Hoffman
 */ 
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SpecificWeekDayRepositoryTests {
    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private SpecificWeekDayRepository weekDayRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private SpecificWeekDayRepository specificWeekDayRepository;

    @BeforeEach
    public void clearDatabase(){
        // Delete the weekDayRepository first to avoid violating not-null constraint
        weekDayRepository.deleteAll();

        // delete the museum mangement system repository
        managerRepository.deleteAll();
        artifactRepository.deleteAll();
        roomRepository.deleteAll();
        specificWeekDayRepository.deleteAll();
        employeeRepository.deleteAll();
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @AfterEach
    public void clearDatabaseAfter(){
        // Delete the weekDayRepository first to avoid violating not-null constraint
        weekDayRepository.deleteAll();

        // delete the museum mangement system repository
        mmsRepository.deleteAll();

    }

    @Test
    public void testPersistAndLoadSpecificWeekDay(){
        // Create a museum management system 
        MuseumManagementSystem mms = new MuseumManagementSystem();

        String name = "WeekDay's MMS";
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

        // Create a Specific Week Day 
        SpecificWeekDay weekDay = new SpecificWeekDay();

        Boolean isClosed = false;
        SpecificWeekDay.DayType dayType = SpecificWeekDay.DayType.Monday;

        weekDay.setIsClosed(isClosed);
        weekDay.setDayType(dayType);
        weekDay.setMuseumManagementSystem(mms);

        weekDayRepository.save(weekDay);

        // Check that everything was saved in the database
        
        // Get the auto generated id of the shift and of the mms
        int mmsId = mms.getSystemId();

        // Set shift, employee and weekDay to null 
        mms = null;
        weekDay = null;

        // get the shift from the database
        weekDay = weekDayRepository.findSpecificWeekDayByDayType(dayType);

        // Assert values of the retrived shift
        assertNotNull(weekDay);
        assertEquals(isClosed, weekDay.getIsClosed());
        assertEquals(dayType, weekDay.getDayType());

        assertNotNull(weekDay.getMuseumManagementSystem());
        assertEquals(mmsId, weekDay.getMuseumManagementSystem().getSystemId());
        assertEquals(name, weekDay.getMuseumManagementSystem().getName());
        assertEquals(openTime, weekDay.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, weekDay.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, weekDay.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, weekDay.getMuseumManagementSystem().getTicketFee());

    }
}
