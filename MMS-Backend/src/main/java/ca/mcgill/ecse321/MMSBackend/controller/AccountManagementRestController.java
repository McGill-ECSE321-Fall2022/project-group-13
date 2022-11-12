package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MMSBackend.dto.ClientDto;
import ca.mcgill.ecse321.MMSBackend.dto.EmployeeDto;
import ca.mcgill.ecse321.MMSBackend.dto.ManagerDto;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Manager;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.service.AccountManagementService;;


/**
 * @author Nikolas Pasichnik
 * 
 */
@CrossOrigin(origins = "*")
@RestController
public class AccountManagementRestController {

    @Autowired
    private AccountManagementService service;

    // -----------------------------GetAllAccount(might not need throws)-------------------------------

    @GetMapping(value = { "/client", "/client/" })
    public List<ClientDto> getAllClients() throws IllegalArgumentException {
        return service.getAllClients().stream().map(p -> ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @GetMapping(value = { "/employee", "/employee/" })
    public List<EmployeeDto> getAllEmployees() throws IllegalArgumentException {
        return service.getAllEmployees().stream().map(p -> ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @GetMapping(value = { "/manager", "/manager/" })
    public List<ManagerDto> getManager() throws IllegalArgumentException {
        return service.getManager().stream().map(p -> ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    // --------------------------------CreateAccount------------------------------------------------

    @PostMapping(value = {"/client", "/client/"})
    public ClientDto createClient(@PathVariable("username") String username, @RequestParam String name, @RequestParam String password, 
    @RequestParam MuseumManagementSystem mms) throws IllegalArgumentException{
        Client client = service.createClient(username, name, password, mms); 
        return ToDtoHelper.convertToDto(client); 
    }

    @PostMapping(value = {"/employee", "/employee/"})
    public EmployeeDto createEmployee(@PathVariable("username") String username, @RequestParam String name, @RequestParam String password, 
    @RequestParam MuseumManagementSystem mms) throws IllegalArgumentException{
        Employee employee = service.createEmployee(username, name, password, mms); 
        return ToDtoHelper.convertToDto(employee); 
    }

    @PostMapping(value = {"/manager", "/manager/"})
    public ManagerDto createManager(@PathVariable("username") String username, @RequestParam String name, @RequestParam String password, 
    @RequestParam MuseumManagementSystem mms) throws IllegalArgumentException{
        Manager manager = service.createManager(username, name, password, mms); 
        return ToDtoHelper.convertToDto(manager); 
    }

    // -------------------------------GetAccount--------------------------------------------------

    @GetMapping(value = {"/client/{username}", "/client/{username}/"})
    public ClientDto getClient(@PathVariable("username") String username) throws IllegalArgumentException {
        Client client = service.getClient(username);
        return ToDtoHelper.convertToDto(client); 
    }

    @GetMapping(value = {"/employee/{username}", "/employee/{username}/"})
    public EmployeeDto getEmployee(@PathVariable("username") String username) throws IllegalArgumentException {
        Employee employee = service.getEmployee(username);
        return ToDtoHelper.convertToDto(employee); 
    }

    @GetMapping(value = {"/manager/{username}", "/manager/{username}/"})
    public ManagerDto getManager(@PathVariable("username") String username) throws IllegalArgumentException {
        Manager manager = service.getManager(username);
        return ToDtoHelper.convertToDto(manager); 
    }

    // -------------------------------DeleteAccount----------------------------------------------

    @DeleteMapping(value = {"/client/{username}", "/client/{username}/"}) //Should i put id??? idk anymore 
    public void deleteClient(@PathVariable("username") String username) throws IllegalArgumentException {
        service.deleteClient(username);
    }

    @DeleteMapping(value = {"/employee/{username}", "/employee/{username}/"}) //Should i put id??? idk anymore 
    public void deleteEmployee(@PathVariable("username") String username) throws IllegalArgumentException {
        service.deleteEmployee(username);
    }

    // -------------------------------SignInAccount----------------------------------------------
    
    



    // --------------------------------EditAccount-----------------------------------------------



}
