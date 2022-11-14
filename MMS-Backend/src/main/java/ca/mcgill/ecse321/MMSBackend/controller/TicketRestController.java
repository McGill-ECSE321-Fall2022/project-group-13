package ca.mcgill.ecse321.MMSBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import ca.mcgill.ecse321.MMSBackend.dto.ClientDto;
import ca.mcgill.ecse321.MMSBackend.dto.TicketDto;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;
import ca.mcgill.ecse321.MMSBackend.service.TicketService;

/**
 * @author Lucy Zhang (Lucy-Zh)
 *         The TicketRestController class is responsible for exposing
 *         the business logic declared in TicketService using a REST
 *         API.
 */

@CrossOrigin(origins = "*")
@RestController
public class TicketRestController {

    @Autowired
    private TicketService ticketService;

    /**
     * Create a new ticket
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param clientDto
     * @return
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/ticket", "/ticket/" })
    public TicketDto createTicket(@RequestParam(name = "client") ClientDto clientDto) throws IllegalArgumentException {

        Ticket ticket = ticketService.createTicket(clientDto.getName());

        return ToDtoHelper.convertToDto(ticket);
    }
    
     /**
     * Get a ticket by id
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param ticketId
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/ticket/{ticketId}", "/ticket/{ticketId}/" })
    public TicketDto getTicket(@PathVariable("ticketId") int ticketId) throws IllegalArgumentException
    {
        Ticket ticket = ticketService.getTicket(ticketId);

        return ToDtoHelper.convertToDto(ticket);
    }

    /**
     * Get a list of all tickets
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param clientDto
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/tickets", "/tickets/" })
    public List<TicketDto> getAllTickets() throws IllegalArgumentException
    {
        List<Ticket> tickets = ticketService.getAllTickets();

        List<TicketDto> ticketDtos = new ArrayList<TicketDto>();

        for (Ticket ticket : tickets) {
            ticketDtos.add(ToDtoHelper.convertToDto(ticket));
        }

        return ticketDtos;
    }

    /**
     * Delete a ticket
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param ticketId
     * @return
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/ticket/{ticketId}", "/ticket/{ticketId}/" })
    public void deleteTicket(@PathVariable("ticketId") int ticketId) throws IllegalArgumentException
    {
        ticketService.deleteTicket(ticketId);
    }

     /**
     * Update a ticket status
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param ticketId
     * @param isActive
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/ticket/{ticketId}", "/ticket/{ticketId}/" })
    public TicketDto updateTicket(@PathVariable("ticketId") int ticketId, @RequestParam boolean isActive) throws IllegalArgumentException
    {
        ticketService.setTicketStatus(ticketId, isActive);

        Ticket ticket = ticketService.getTicket(ticketId);

        return ToDtoHelper.convertToDto(ticket);

    }

    /**
     * Get tickets by client
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param ticketId
     * @param clientUsername
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/tickets/{clientUsername}", "/tickets/{clientUsername}/" })
    public List<TicketDto> getTicketsByClient(@PathVariable("clientUsername") String clientUsername) throws IllegalArgumentException
    {
        List<Ticket> tickets = ticketService.getAllTicketsByClient(clientUsername);

        List<TicketDto> ticketDtos = new ArrayList<TicketDto>();

        for (Ticket ticket : tickets) {
            ticketDtos.add(ToDtoHelper.convertToDto(ticket));
        }

        return ticketDtos;
    }
}
