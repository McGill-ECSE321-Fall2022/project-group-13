package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.dto.ClientDto;
import ca.mcgill.ecse321.MMSBackend.dto.MuseumManagementSystemDto;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
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

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse321.MMSBackend.dto.TicketDto;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;
import ca.mcgill.ecse321.MMSBackend.service.ClientAccountService;
import ca.mcgill.ecse321.MMSBackend.service.TicketService;

/**
 * @author Lucy Zhang (Lucy-Zh)
 * The TicketRestController class is responsible for exposing
 * the business logic declared in TicketService using a REST
 * API.
 */
@CrossOrigin(origins = "*")
@RestController
public class TicketRestController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ClientAccountService clientService;

    /**
     * Create a new ticket
     * 
     * @Author Lucy Zhang (Lucy-Zh)
     * @param clientUsername
     * @return clientUsername
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/ticket", "/ticket/" })
    public ResponseEntity<TicketDto> createTicket(@RequestParam String clientUsername) throws IllegalArgumentException {
        Ticket ticket = ticketService.createTicket(clientUsername);
        return new ResponseEntity<>(convertToDto(ticket), HttpStatus.CREATED);
    }

    /**
     * Get a ticket by id
     * 
     * @Author Lucy Zhang (Lucy-Zh)
     * @param ticketId
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/ticket/{ticketId}", "/ticket/{ticketId}/" })
    public ResponseEntity<TicketDto> getTicket(@PathVariable("ticketId") int ticketId) throws IllegalArgumentException {
        Ticket ticket = ticketService.getTicket(ticketId);
        return new ResponseEntity<>(convertToDto(ticket), HttpStatus.OK);
    }

    /**
     * Get a list of all tickets
     * 
     * @Author Lucy Zhang (Lucy-Zh)
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/tickets", "/tickets/" })
    public ResponseEntity<List<TicketDto>> getAllTickets() throws IllegalArgumentException {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<TicketDto> ticketDtos = new ArrayList<>();

        for (Ticket ticket : tickets) {
            ticketDtos.add(convertToDto(ticket));
        }
        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }

    /**
     * Delete a ticket
     * 
     * @Author Lucy Zhang (Lucy-Zh)
     * @param ticketId
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/ticket/{ticketId}", "/ticket/{ticketId}/" })
    public void deleteTicket(@PathVariable("ticketId") int ticketId) throws IllegalArgumentException {
        ticketService.deleteTicket(ticketId);
    }

    /**
     * Update a ticket status
     * 
     * @Author Lucy Zhang (Lucy-Zh)
     * @param ticketId
     * @param isActive
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/ticket/{ticketId}", "/ticket/{ticketId}/" })
    public ResponseEntity<TicketDto> updateTicket(@PathVariable("ticketId") int ticketId,
            @RequestParam boolean isActive) throws IllegalArgumentException {
        Ticket ticket = ticketService.getTicket(ticketId);
        ticketService.setTicketStatus(ticket, isActive);
        return new ResponseEntity<>(convertToDto(ticket), HttpStatus.OK);
    }

    /**
     * Get tickets by client
     * 
     * @Author Lucy Zhang (Lucy-Zh)
     * @param clientUsername
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/tickets/{clientUsername}", "/tickets/{clientUsername}/" })
    public ResponseEntity<List<TicketDto>> getTicketsByClient(@PathVariable("clientUsername") String clientUsername)
            throws IllegalArgumentException {
        Client client = clientService.getClient(clientUsername);
        List<Ticket> tickets = ticketService.getAllTicketsByClient(client);
        List<TicketDto> ticketDtos = new ArrayList<>();

        for (Ticket ticket : tickets) {
            ticketDtos.add(convertToDto(ticket));
        }
        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }

    // Helper methods to convert classes from the model into DTOs and vice-versa

    private TicketDto convertToDto(Ticket ticket)
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("There is no such Ticket!");
        }
        return new TicketDto(ticket.getTicketId(), ticket.getFee(), ticket.getIsActive(), convertToDto(ticket.getMuseumManagementSystem()), convertToDto(ticket.getClient()));
    }

    private MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms) {
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        return new MuseumManagementSystemDto(mms.getSystemId(), mms.getName(), mms.getOpenTime(), mms.getCloseTime(), mms.getMaxLoanNumber(), mms.getTicketFee());
    }

    private ClientDto convertToDto(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("There is no such Client!");
        }
        MuseumManagementSystemDto mmsDto = convertToDto(client.getMuseumManagementSystem());

        return new ClientDto(client.getUsername(), client.getName(), client.getPassword(),
                client.getCurrentLoanNumber(), mmsDto);
    }
}
