package ca.mcgill.ecse321.MMSBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse321.MMSBackend.dao.TicketRepository;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    /**
     * Create ticket
     */
    @Transactional
    public Ticket createTicket(Client client) {
        Ticket ticket = new Ticket();
        ticket.setFee(client.getMuseumManagementSystem().getTicketFee());
        ticket.setClient(client);
        ticket.setMuseumManagementSystem(client.getMuseumManagementSystem());
        ticket.setIsActive(true);
        ticketRepository.save(ticket);
        return ticket;
    }

    /**
     * Get ticket
     *
     */
    @Transactional
    public Ticket getTicket(int ticketId) {

        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);

        if(ticket == null) {
            throw new IllegalArgumentException("Ticket does not exist");
        }else{
            return ticket;
        }
        
    }

    /**
     * Get all tickets
     */
    @Transactional
    public Iterable<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    /**
     * Delete ticket
     */
    @Transactional
    public void deleteTicket(int ticketId) {

        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);

        if(ticket == null) {
            throw new IllegalArgumentException("Ticket does not exist");
        }else{
            ticketRepository.delete(ticket);
        }
    }

    /**
     * Set ticket status
     */
    @Transactional
    public void setTicketStatus(int ticketId, boolean status) {

        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);
        
        if(ticket == null) {
            throw new IllegalArgumentException("Ticket does not exist");
        }else{
            ticket.setIsActive(status);
        }
    }

    /**
     * Get ticket status
     */
    @Transactional
    public void getTicketStatus(int ticketId) {

        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);

        if(ticket == null) {
            throw new IllegalArgumentException("Ticket does not exist");
        }else{
            ticket.getIsActive();
        }
     
    }

    /**
     * Get all tickets by client
     */
    public List<Ticket> getAllTicketsByClient(Client client){
        
        List<Ticket> ticketsByClient = new ArrayList<>();
        Iterable<Ticket> tickets = ticketRepository.findAll();
        
        for(Ticket ticket : tickets){
            if(ticket.getClient().equals(client)){
                ticketsByClient.add(ticket);
            }
        }

        return ticketsByClient;
    }

}
