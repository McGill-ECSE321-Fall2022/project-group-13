package ca.mcgill.ecse321.MMSBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.TicketRepository;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    MuseumManagementSystemRepository museumManagementSystemRepository;

    /**
     * Create ticket for a client
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     * @param clientUsername
     */
    @Transactional
    public Ticket createTicket(String clientUsername) {
        Client client = clientRepository.findClientByUsername(clientUsername);
        Ticket ticket = new Ticket();
        ticket.setFee(client.getMuseumManagementSystem().getTicketFee());
        ticket.setClient(client);
        ticket.setMuseumManagementSystem(client.getMuseumManagementSystem());
        ticket.setIsActive(true);
        ticketRepository.save(ticket);
        return ticket;
    }

    /**
     * Get ticket through id
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     * @param ticketId
     *
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
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     */
    @Transactional
    public Iterable<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    /**
     * Get all tickets for a specific museum
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     * @param museumSystemId
     *
     */

    public List<Ticket> getAllTicketsByMuseum(int museumSystemId){
        
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumSystemId);

        if(museumManagementSystem == null) {
            throw new IllegalArgumentException("Museum management system does not exist");
        }else{

            List<Ticket> ticketsByMuseum = new ArrayList<>();
            Iterable<Ticket> tickets = ticketRepository.findAll();
            
            for(Ticket ticket : tickets){
                if(ticket.getMuseumManagementSystem().equals(museumManagementSystem)){
                    ticketsByMuseum.add(ticket);
                }
            }

            return ticketsByMuseum;

        }
    }

    /**
     * Delete ticket
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     * @param ticketId
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
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     * @param ticketId, status
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
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     * @param ticketId
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
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     * @param clientUsername
     */
    public List<Ticket> getAllTicketsByClient(String clientUsername){
        
        Client client = clientRepository.findClientByUsername(clientUsername); 

        if(client == null) {
            throw new IllegalArgumentException("Client does not exist");
        }else{

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

}
