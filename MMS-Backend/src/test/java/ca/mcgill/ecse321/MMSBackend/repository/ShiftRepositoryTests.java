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

import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Shift;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;

/**
 * @author Samantha Perez Hoffman
 */ 
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShiftRepositoryTests {
    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private EmployeeRepository employeeRepository; 

    @Autowired
    private SpecificWeekDayRepository weekDayRepository;

    @Autowired
    private ShiftRepository shiftRepository;

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
        // Delete the shiftRepository first to avoid violating not-null constraint
        shiftRepository.deleteAll();

        // delete the employeeRepository and weekDayRepository  
        weekDayRepository.deleteAll(); 
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
        artifactRepository.deleteAll();
        roomRepository.deleteAll();
        specificWeekDayRepository.deleteAll();
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @AfterEach
    public void clearDatabaseAfter(){
        // Delete the shiftRepository first to avoid violating not-null constraint
        shiftRepository.deleteAll();

        // delete the employeeRepository and weekDayRepository
        weekDayRepository.deleteAll();
        employeeRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadShift(){
        // Create a museum management system 
        MuseumManagementSystem mms = new MuseumManagementSystem();
        String mmsName = "Shift's MMS";
        Time openTime = Time.valueOf("9:00:00");
        Time closeTime = Time.valueOf("17:00:00");
        int maxLoanNumber = 5;
        double ticketFee = 13.25;
        
        mms.setName(mmsName);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);
        
        // Save the museum management system in the database
        mmsRepository.save(mms);

        // Create an Employee
        Employee employee = new Employee();
        String employeeUsername = "ShiftUsername";
        String employeeName = "Shift Zhang";
        String employeePassword = "ShiftPass";

        employee.setUsername(employeeUsername);
        employee.setName(employeeName);
        employee.setPassword(employeePassword);
        employee.setMuseumManagementSystem(mms);

        // Save the employee in the database
        employeeRepository.save(employee);

        // Create a Specific Week Day 
        SpecificWeekDay weekDay = new SpecificWeekDay();
        Boolean weekDayIsClosed = false;
        DayType type = DayType.Friday;

        weekDay.setIsClosed(weekDayIsClosed);
        weekDay.setDayType(type);
        weekDay.setMuseumManagementSystem(mms);
        
        // Save the specific week day in the database
        weekDayRepository.save(weekDay);

        //  Create a shift 
        Shift shift = new Shift();
        Time startTime = Time.valueOf("9:00:00");
        Time endTime = Time.valueOf("17:00:00");

        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
        shift.setEmployee(employee);
        shift.setDayOfTheWeek(weekDay);
        shift.setMuseumManagementSystem(mms);

        // Save the shift in the database
        shiftRepository.save(shift);

        // Check that everything was saved in the database
        
        // Get the auto generated id of the shift and of the mms
        Integer mmsId = mms.getSystemId();
        Integer shiftId = shift.getShiftId();

        // Set shift, employee and weekDay to null 
        shift = null;
        employee = null;
        weekDay = null;
        mms = null;

        // get the shift from the database
        shift = shiftRepository.findShiftByShiftId(shiftId);

        // Assert values of the retrived shift
        assertNotNull(shift);
        assertEquals(shiftId, shift.getShiftId());
        assertEquals(startTime, shift.getStartTime());
        assertEquals(endTime, shift.getEndTime());

        assertNotNull(shift.getMuseumManagementSystem());
        assertEquals(mmsId, shift.getMuseumManagementSystem().getSystemId());
        assertEquals(mmsName, shift.getMuseumManagementSystem().getName());
        assertEquals(openTime, shift.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, shift.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, shift.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, shift.getMuseumManagementSystem().getTicketFee());


        assertNotNull(shift.getEmployee());
        assertEquals(employeeUsername, shift.getEmployee().getUsername());
        assertEquals(employeeName, shift.getEmployee().getName());
        assertEquals(employeePassword, shift.getEmployee().getPassword());
        assertNotNull(shift.getEmployee().getMuseumManagementSystem());
        assertEquals(shift.getMuseumManagementSystem(), shift.getEmployee().getMuseumManagementSystem());

        assertNotNull(shift.getDayOfTheWeek());
        assertEquals(type, shift.getDayOfTheWeek().getDayType());
        assertEquals(weekDayIsClosed, shift.getDayOfTheWeek().getIsClosed());
        assertNotNull(shift.getDayOfTheWeek().getMuseumManagementSystem());
        assertEquals(shift.getMuseumManagementSystem(), shift.getDayOfTheWeek().getMuseumManagementSystem());

    }
}
