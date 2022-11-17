package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MMSBackend.dto.ManagerDto;
import ca.mcgill.ecse321.MMSBackend.model.Manager;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.service.ManagerAccountService;;


/**
 * @author Nikolas Pasichnik
 * 
 */
@CrossOrigin(origins = "*")
@RestController
public class ManagerAccountRestController {

    @Autowired
    private ManagerAccountService service ; 

    @GetMapping(value = { "/managers", "/managers/" })
    public ManagerDto getManager() throws IllegalArgumentException {
        return ToDtoHelper.convertToDto(service.getManager()); 
    }

    @PostMapping(value = {"/manager", "/manager/"})
    public ManagerDto createManager(@PathVariable("username") String username, @RequestParam String name, @RequestParam String password, 
    @RequestParam MuseumManagementSystem mms) throws IllegalArgumentException{
        Manager manager = service.createManager(username, name, password, mms); 
        return ToDtoHelper.convertToDto(manager); 
    }

    @GetMapping(value = {"/manager/signin/{username}", "/manager/signin/{username}/"})
    public ManagerDto signInManagerAccount(@PathVariable("username") String username, @RequestParam String password) throws IllegalArgumentException{
        Manager manager = service.signInManagerAccount(username, password);

        return ToDtoHelper.convertToDto(manager);
    }   

    @PutMapping(value = { "/manager/{id}", "/manager/{id}/" })
    public ManagerDto editManager(@PathVariable("username") String username, @RequestParam String name, @RequestParam String
        password) throws IllegalArgumentException{

        Manager manager = service.editManagerAccount(name, password);
        return ToDtoHelper.convertToDto(manager);
    }
}
