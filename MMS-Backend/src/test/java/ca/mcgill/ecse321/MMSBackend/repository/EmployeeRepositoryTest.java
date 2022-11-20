package ca.mcgill.ecse321.MMSBackend.repository;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.dao.EmployeeRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Nikolas Pasichnik (NikolasPasichnik)
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeRepositoryTest {

    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    public void clearDatabase() {
        // Delete the employee first to avoid violating not-null constraints
        employeeRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testEmployeeRepository() {

        // Creating a museum management system using a plain constructor
        MuseumManagementSystem mms = new MuseumManagementSystem();
        String museumName = "Employee's MMS";
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

        // Creating a employees
        Employee employee = new Employee();
        String username = "EmployeeZhang";
        String name = "Employee Zhang";
        String password = "employee223";

        employee.setUsername(username);
        employee.setName(name);
        employee.setPassword(password);
        employee.setMuseumManagementSystem(mms);

        // Saving the manager to the database
        employeeRepository.save(employee);

        // Getting ids of Manager and mms
        String employeeUsername = employee.getUsername();
        int museumID = mms.getSystemId();

        // Make the variables null
        mms = null;
        employee = null;

        // Fetching information from the database
        employee = employeeRepository.findEmployeeByUsername(employeeUsername);

        // Checking for existence of manager and valid username
        assertNotNull(employee);
        assertEquals(employeeUsername, employee.getUsername());

        // Checking for proper connection between museum and manager
        assertNotNull(employee.getMuseumManagementSystem());
        assertEquals(museumID, employee.getMuseumManagementSystem().getSystemId());
        assertEquals(museumName, employee.getMuseumManagementSystem().getName());
        assertEquals(openTime, employee.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, employee.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, employee.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, employee.getMuseumManagementSystem().getTicketFee());

    }

}
