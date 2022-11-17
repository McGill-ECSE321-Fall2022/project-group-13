package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * @author Nikolas Pasichnik
 * 
 */
@CrossOrigin(origins = "*")
@RestController
public class EmployeeAccountRestController {

    @Autowired
    private EmployeeAccountService service; 

    @GetMapping(value = { "/employees", "/employees/" })
    public List<EmployeeDto> getAllEmployees() throws IllegalArgumentException {
        return service.getAllEmployees().stream().map(p -> ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @PostMapping(value = {"/employee", "/employee/"})
    public EmployeeDto createEmployee(@PathVariable("username") String username, @RequestParam String name, @RequestParam String password, 
    @RequestParam MuseumManagementSystem mms) throws IllegalArgumentException{
        Employee employee = service.createEmployee(username, name, password, mms); 
        return ToDtoHelper.convertToDto(employee); 
    }

    @GetMapping(value = {"/employee/{username}", "/employee/{username}/"})
    public EmployeeDto getEmployee(@PathVariable("username") String username) throws IllegalArgumentException {
        Employee employee = service.getEmployee(username);
        return ToDtoHelper.convertToDto(employee); 
    }

    @DeleteMapping(value = {"/employee/{username}", "/employee/{username}/"}) //Should i put id??? idk anymore 
    public void deleteEmployee(@PathVariable("username") String username) throws IllegalArgumentException {
        service.deleteEmployee(username);
    }

    @GetMapping(value = {"/employee/signin/{username}", "/employee/signin/{username}/"})
    public EmployeeDto signInEmployeeAccount(@PathVariable("username") String username, @RequestParam String password) throws IllegalArgumentException{
        Employee employee = service.signInEmployeeAccount(username, password);

        return ToDtoHelper.convertToDto(employee);
    }   
    
    @PutMapping(value = { "/employee/{id}", "/employee/{id}/" })
    public EmployeeDto editEmployee(@PathVariable("username") String username, @RequestParam String name, @RequestParam String
        password) throws IllegalArgumentException{

        Employee employee = service.editEmployeeAccount(username, name, password);
        return ToDtoHelper.convertToDto(employee);
    }
}
