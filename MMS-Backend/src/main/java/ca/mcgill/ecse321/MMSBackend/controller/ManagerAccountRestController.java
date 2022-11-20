package ca.mcgill.ecse321.MMSBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ca.mcgill.ecse321.MMSBackend.service.ManagerAccountService;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;;


/**
 * @author Nikolas Pasichnik (NikolasPasichnik)
 * 
 *         The ManagerAccountRestController class is responsible for exposing
 *         the business logic declared in ManagerAccountService using a REST
 *         API.
 * 
 */
@CrossOrigin(origins = "*")
@RestController
public class ManagerAccountRestController {

    @Autowired
    private ManagerAccountService service; 
    
    @Autowired
    private MuseumManagementSystemService mmsService; 

    @GetMapping(value = { "/managers", "/managers/" })
    public ResponseEntity<ManagerDto> getManager() throws IllegalArgumentException {
        return new ResponseEntity<>(ToDtoHelper.convertToDto(service.getManager()), HttpStatus.OK);
    }

    @PostMapping(value = {"/manager", "/manager/"})
    public ResponseEntity<ManagerDto> createManager(@RequestParam String username, @RequestParam String name, @RequestParam String password, 
    @RequestParam int systemId) throws IllegalArgumentException{
        MuseumManagementSystem mms = mmsService.getMuseumManagementSystem(systemId);
        Manager manager = service.createManager(username, name, password, mms); 
        return new ResponseEntity<ManagerDto>(ToDtoHelper.convertToDto(manager), HttpStatus.OK); 
    }

    @GetMapping(value = {"/manager/signin/{username}", "/manager/signin/{username}/"})
    public ResponseEntity<ManagerDto> signInManagerAccount(@PathVariable("username") String username, @RequestParam String password) throws IllegalArgumentException{
        Manager manager = service.signInManagerAccount(username, password);
        return new ResponseEntity<ManagerDto>(ToDtoHelper.convertToDto(manager), HttpStatus.OK); 
    }   

    @PutMapping(value = { "/manager/edit/{username}", "/manager/edit/{username}/" })
    public ResponseEntity<ManagerDto> editManager(@PathVariable("username") String username, @RequestParam String name, @RequestParam String
        password) throws IllegalArgumentException{
        Manager manager = service.editManagerAccount(name, password);
        return new ResponseEntity<ManagerDto>(ToDtoHelper.convertToDto(manager), HttpStatus.OK); 
    }
}
