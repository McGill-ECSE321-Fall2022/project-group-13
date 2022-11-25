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

import ca.mcgill.ecse321.MMSBackend.dto.ClientDto;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.service.ClientAccountService;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;

/**
 * @author Nikolas Pasichnik (NikolasPasichnik)
 * 
 *         The ClientAccountRestController class is responsible for exposing
 *         the business logic declared in ClientAccountService using a REST
 *         API.
 * 
 */
@CrossOrigin(origins = "*")
@RestController
public class ClientAccountRestController {

    @Autowired
    private ClientAccountService service;

    @Autowired
    private MuseumManagementSystemService mmsService;

    @Autowired
    private ToDtoHelper toDtoHelper;

    @GetMapping(value = { "/clients", "/clients/" })
    public ResponseEntity<List<ClientDto>> getAllClients() throws IllegalArgumentException {
        return new ResponseEntity<>(
                service.getAllClients().stream().map(p -> toDtoHelper.convertToDto(p)).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PostMapping(value = { "/client", "/client/" })
    public ResponseEntity<ClientDto> createClient(@RequestParam String username, @RequestParam String name,
            @RequestParam String password,
            @RequestParam int systemId) throws IllegalArgumentException {
        MuseumManagementSystem mms = mmsService.getMuseumManagementSystem(systemId);
        Client client = service.createClient(username, name, password, mms);
        return new ResponseEntity<ClientDto>(toDtoHelper.convertToDto(client), HttpStatus.CREATED);
    }

    @GetMapping(value = { "/client/{username}", "/client/{username}/" })
    public ResponseEntity<ClientDto> getClient(@PathVariable("username") String username)
            throws IllegalArgumentException {
        Client client = service.getClient(username);
        return new ResponseEntity<ClientDto>(toDtoHelper.convertToDto(client), HttpStatus.OK);
    }

    @DeleteMapping(value = { "/client/{username}", "/client/{username}/" })
    public void deleteClient(@PathVariable("username") String username) throws IllegalArgumentException {
        service.deleteClient(username);
    }

    @GetMapping(value = { "/client/signin/{username}", "/client/signin/{username}/" })
    public ResponseEntity<ClientDto> signInClientAccount(@PathVariable("username") String username,
            @RequestParam String password) throws IllegalArgumentException {
        Client client = service.signInClientAccount(username, password);
        return new ResponseEntity<ClientDto>(toDtoHelper.convertToDto(client), HttpStatus.OK);
    }

    @PutMapping(value = { "/client/edit/{username}", "/client/edit/{username}/" })
    public ResponseEntity<ClientDto> editClient(@PathVariable("username") String username, @RequestParam String name,
            @RequestParam String password) throws IllegalArgumentException {
        Client client = service.editClientAccount(username, name, password);
        return new ResponseEntity<ClientDto>(toDtoHelper.convertToDto(client), HttpStatus.OK);
    }
}
