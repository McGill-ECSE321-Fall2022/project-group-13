package ca.mcgill.ecse321.MMSBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.TicketRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

@Service
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

        if (clientUsername.equals("") || clientUsername.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

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
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Ticket does not exist");
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
    public List<Ticket> getAllTickets() {
        return toList(ticketRepository.findAll());
    }

    /**
     * Get all tickets for a specific museum
     * 
     * @Author : Lucy Zhang (Lucy-Zh)
     * @param museumSystemId
     *
     */

    @Transactional
    public List<Ticket> getAllTicketsByMuseum(int museumSystemId){
        
        MuseumManagementSystem museumManagementSystem = museumManagementSystemRepository.findMuseumManagementSystemBySystemId(museumSystemId);

        if(museumManagementSystem == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Ticket does not exist");
        }else{

            List<Ticket> tickets = getAllTickets();
            
            for(Ticket ticket : tickets){
                if(!ticket.getMuseumManagementSystem().equals(museumManagementSystem)){
                    tickets.remove(ticket);
                }
            }

            return tickets;

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
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Ticket does not exist");
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
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Ticket does not exist");
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
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Ticket does not exist");
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

    @Transactional
    public List<Ticket> getAllTicketsByClient(String clientUsername){
        
        Client client = clientRepository.findClientByUsername(clientUsername); 

        if(client == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Ticket does not exist");
        }else{

            List<Ticket> tickets = getAllTickets();
            
            for(Ticket ticket : tickets){
                if(!ticket.getClient().equals(client)){
                    tickets.remove(ticket);
                }
            }

            return tickets;

        }
    }

    /**
     * toList helper method (@author eventRegistration authors)
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}
