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
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

/**
 * @author Lucy Zhang (Lucy-Zh)
 *         The TicketService class implements the use case based on
 *         the following requirement:
 *         “FREQ05: The museum management system shall generate and display to
 *         the manager and the employees a list of all tickets
 *         purchased and their information, which includes the purchaser’s
 *         username, ticket fee at the time of the purchase, and
 *         whether the ticket has been used.”
 */
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
     * @author Lucy Zhang (Lucy-Zh)
     * @param clientUsername
     */
    @Transactional
    public Ticket createTicket(String clientUsername) {
        if (clientUsername.equals("") || clientUsername.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "This username is invalid");
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
     * @author Lucy Zhang (Lucy-Zh)
     * @param ticketId
     *
     */
    @Transactional
    public Ticket getTicket(int ticketId) {
        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);
        if (ticket == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Ticket does not exist");
        } else {
            return ticket;
        }

    }

    /**
     * Get all tickets
     * 
     * @author Lucy Zhang (Lucy-Zh)
     */
    @Transactional
    public List<Ticket> getAllTickets() {
        return toList(ticketRepository.findAll());
    }

    /**
     * Delete ticket
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @param ticketId
     */
    @Transactional
    public void deleteTicket(int ticketId) {
        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);
        if (ticket == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Ticket does not exist");
        } else {
            ticketRepository.delete(ticket);
        }
    }

    /**
     * Set ticket status
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @param ticketId, status
     */
    @Transactional
    public void setTicketStatus(Ticket ticket, boolean status) {
        if (ticket == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Ticket does not exist");
        } else {
            ticket.setIsActive(status);
        }
    }

    /**
     * Get all tickets by client
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @param clientUsername
     */
    @Transactional
    public List<Ticket> getAllTicketsByClient(Client client) {
        if (client == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Ticket does not exist");
        } else {
            List<Ticket> tickets = getAllTickets();
            List<Ticket> returnedTickets = new ArrayList<Ticket>();

            for (Ticket ticket : tickets) {
                if (ticket.getClient().equals(client)) {
                    returnedTickets.add(ticket);
                }
            }
            return returnedTickets;
        }
    }

    /**
     * toList helper method 
     * @author eventRegistration authors 
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}
