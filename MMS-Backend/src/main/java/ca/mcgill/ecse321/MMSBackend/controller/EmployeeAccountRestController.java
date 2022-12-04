package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.MMSBackend.dto.MuseumManagementSystemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MMSBackend.dto.EmployeeDto;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.service.EmployeeAccountService;
import ca.mcgill.ecse321.MMSBackend.service.EmployeeScheduleService;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;

/**
 * @author Nikolas Pasichnik (NikolasPasichnik)
 * 
 * The EmployeeAccountRestController class is responsible for exposing
 * the business logic declared in EmployeeAccountService using a REST
 * API.
 */
@CrossOrigin(origins = "*")
@RestController
public class EmployeeAccountRestController {

    @Autowired
    private EmployeeAccountService service;

    @Autowired
    private MuseumManagementSystemService mmsService;

    @Autowired
    private EmployeeScheduleService shiftService; 

    @GetMapping(value = { "/employees", "/employees/" })
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() throws IllegalArgumentException {
        return new ResponseEntity<>(
                service.getAllEmployees().stream().map(this::convertToDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PostMapping(value = { "/employee", "/employee/" })
    public ResponseEntity<EmployeeDto> createEmployee(@RequestParam String username, @RequestParam String name,
            @RequestParam String password) throws IllegalArgumentException {
        MuseumManagementSystem mms = mmsService.getMuseumManagementSystem();
        Employee employee = service.createEmployee(username, name, password, mms);
        return new ResponseEntity<EmployeeDto>(convertToDto(employee), HttpStatus.OK);
    }

    @GetMapping(value = { "/employee/{username}", "/employee/{username}/" })
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("username") String username)
            throws IllegalArgumentException {
        Employee employee = service.getEmployee(username);
        return new ResponseEntity<EmployeeDto>(convertToDto(employee), HttpStatus.OK);
    }

    @DeleteMapping(value = { "/employee/delete/{username}", "/employee/delete/{username}/" })
    public void deleteEmployee(@PathVariable("username") String username) throws IllegalArgumentException {
        Employee employee = service.getEmployee(username);
        shiftService.deleteAllShiftsForEmployee(employee);
        service.deleteEmployee(username);
    }

    @GetMapping(value = { "/employee/signin/{username}", "/employee/signin/{username}/" })
    public ResponseEntity<EmployeeDto> signInEmployeeAccount(@PathVariable("username") String username,
            @RequestParam String password) throws IllegalArgumentException {
        Employee employee = service.signInEmployeeAccount(username, password);
        return new ResponseEntity<EmployeeDto>(convertToDto(employee), HttpStatus.OK);
    }

    @PutMapping(value = { "/employee/edit/{username}", "/employee/edit/{username}/" })
    public ResponseEntity<EmployeeDto> editEmployee(@PathVariable("username") String username,
            @RequestParam String name, @RequestParam String password) throws IllegalArgumentException {
        Employee employee = service.editEmployeeAccount(username, name, password);
        return new ResponseEntity<EmployeeDto>(convertToDto(employee), HttpStatus.OK);
    }

    // Helper methods to convert classes from the model into DTOs and vice-versa

    private EmployeeDto convertToDto(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException("There is no such Employee!");
        }
        MuseumManagementSystemDto mmsDto = convertToDto(employee.getMuseumManagementSystem());

        return new EmployeeDto(employee.getUsername(), employee.getName(),
                employee.getPassword(), mmsDto);
    }

    private MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms) {
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        return new MuseumManagementSystemDto(mms.getSystemId(), mms.getName(), mms.getOpenTime(), mms.getCloseTime(), mms.getMaxLoanNumber(), mms.getTicketFee());
    }
}
