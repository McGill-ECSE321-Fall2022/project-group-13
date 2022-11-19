package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;

/**
 * @author Nikolas Pasichnik
 * 
 */
@CrossOrigin(origins = "*")
@RestController
public class EmployeeAccountRestController {

    @Autowired
    private EmployeeAccountService service;

    @Autowired
    private MuseumManagementSystemService mmsService; 

    @GetMapping(value = { "/employees", "/employees/" })
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() throws IllegalArgumentException {
        return new ResponseEntity<>(service.getAllEmployees().stream().map(p -> ToDtoHelper.convertToDto(p)).collect(Collectors.toList()), HttpStatus.OK); 
    }

    @PostMapping(value = {"/employee", "/employee/"})
    public ResponseEntity<EmployeeDto> createEmployee(@RequestParam String username, @RequestParam String name, @RequestParam String password, 
    @RequestParam int systemId) throws IllegalArgumentException{
        MuseumManagementSystem mms = mmsService.getMuseumManagementSystem(systemId);
        Employee employee = service.createEmployee(username, name, password, mms); 
        return new ResponseEntity<EmployeeDto>(ToDtoHelper.convertToDto(employee), HttpStatus.OK); 
    }

    @GetMapping(value = {"/employee/{username}", "/employee/{username}/"})
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("username") String username) throws IllegalArgumentException {
        Employee employee = service.getEmployee(username);
        return new ResponseEntity<EmployeeDto>(ToDtoHelper.convertToDto(employee), HttpStatus.OK); 
    }

    @DeleteMapping(value = {"/employee/{username}", "/employee/{username}/"}) 
    public void deleteEmployee(@PathVariable("username") String username) throws IllegalArgumentException {
        service.deleteEmployee(username);
    }

    @GetMapping(value = {"/employee/signin/{username}", "/employee/signin/{username}/"})
    public ResponseEntity<EmployeeDto> signInEmployeeAccount(@PathVariable("username") String username, @RequestParam String password) throws IllegalArgumentException{
        Employee employee = service.signInEmployeeAccount(username, password);
        return new ResponseEntity<EmployeeDto>(ToDtoHelper.convertToDto(employee), HttpStatus.OK); 
    }   
    
    @PutMapping(value = { "/employee/edit/{username}", "/employee/edit/{username}/" })
    public ResponseEntity<EmployeeDto> editEmployee(@PathVariable("username") String username, @RequestParam String name, @RequestParam String
        password) throws IllegalArgumentException{
        Employee employee = service.editEmployeeAccount(username, name, password);
        return new ResponseEntity<EmployeeDto>(ToDtoHelper.convertToDto(employee), HttpStatus.OK); 
    }
}
