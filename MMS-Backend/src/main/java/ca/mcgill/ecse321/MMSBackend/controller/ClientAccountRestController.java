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

import ca.mcgill.ecse321.MMSBackend.dto.ClientDto;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.service.ClientAccountService;


/**
 * @author Nikolas Pasichnik
 * 
 */
@CrossOrigin(origins = "*")
@RestController
public class ClientAccountRestController {

    @Autowired
    private ClientAccountService service; 
    
    @GetMapping(value = { "/clients", "/clients/" })
    public List<ClientDto> getAllClients() throws IllegalArgumentException {
        return service.getAllClients().stream().map(p -> ToDtoHelper.convertToDto(p)).collect(Collectors.toList());
    }

    @PostMapping(value = {"/client", "/client/"})
    public ClientDto createClient(@PathVariable("username") String username, @RequestParam String name, @RequestParam String password, 
    @RequestParam MuseumManagementSystem mms) throws IllegalArgumentException{
        Client client = service.createClient(username, name, password, mms); 
        return ToDtoHelper.convertToDto(client); 
    }

    @GetMapping(value = {"/client/{username}", "/client/{username}/"})
    public ClientDto getClient(@PathVariable("username") String username) throws IllegalArgumentException {
        Client client = service.getClient(username);
        return ToDtoHelper.convertToDto(client); 
    }

    @DeleteMapping(value = {"/client/{username}", "/client/{username}/"}) //Should i put id??? idk anymore 
    public void deleteClient(@PathVariable("username") String username) throws IllegalArgumentException {
        service.deleteClient(username);
    }

    @GetMapping(value = {"/client/signin/{username}", "/client/signin/{username}/"})
    public ClientDto signInClientAccount(@PathVariable("username") String username, @RequestParam String password) throws IllegalArgumentException{
        Client client = service.signInClientAccount(username, password);

        return ToDtoHelper.convertToDto(client);
    }  
    
    @PutMapping(value = { "/client/{id}", "/client/{id}/" })
    public ClientDto editClient(@PathVariable("username") String username, @RequestParam String name, @RequestParam String
        password) throws IllegalArgumentException{

        Client client = service.editClientAccount(username, name, password);
        return ToDtoHelper.convertToDto(client);
    }
}
