package ca.mcgill.ecse321.MMSBackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.TicketRepository;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TicketRepositoryTests {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private ClientRepository clientRepository;
    
    @AfterEach
    public void clearDatabase(){
        
        // Delete the ticketRepository first to avoid violating not-null constraint
        ticketRepository.deleteAll();

        // delete the clientRepository and mmsRepository   
        clientRepository.deleteAll();
        mmsRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadTicket(){

        // Create a museum management system
        MuseumManagementSystem mms = new MuseumManagementSystem();

        String name = "Tickets MMS";
        Time openTime = Time.valueOf("9:00:00");
        Time closeTime = Time.valueOf("17:00:00");
        int maxLoanNumber = 5;
        double ticketFee = 13.25;
        
        // Set the attributes of the museum management system
        mms.setName(name);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);

        // Save the museum management system to the database
        mmsRepository.save(mms);

        // Create a client
        Client client = new Client();
        String username = "Ticket Client";
        String clientName = "Ticket ClientLastName";
        String clientPassword = "TicketClient";
        int currentLoanNumber = 2;

        // Set the attributes of the client
        client.setUsername(username);
        client.setName(clientName);
        client.setPassword(clientPassword);
        client.setCurrentLoanNumber(currentLoanNumber);
        client.setMuseumManagementSystem(mms);

        // Save the client to the database
        clientRepository.save(client);

        //Create a ticket
        Ticket ticket = new Ticket();
        double fee = 50;
        boolean isActive = true;

        // Set the attributes of the ticket
        ticket.setFee(fee);
        ticket.setIsActive(isActive);
        ticket.setMuseumManagementSystem(mms);
        ticket.setClient(client);

        // Save the ticket to the database
        ticketRepository.save(ticket);

        //Set the id of the ticket and mms
        Integer mmsId = mms.getSystemId();
        Integer ticketId = ticket.getTicketId();

        mms = null;
        client = null;
        ticket = null;

        //get ticket from database
        ticket = ticketRepository.findTicketByTicketId(ticketId);

        // Check if the ticket is not null
        assertNotNull(ticket);

        // Check if the ticket has the correct attributes
        assertEquals(ticketId, ticket.getTicketId());
        assertEquals(fee, ticket.getFee());
        assertEquals(isActive, ticket.getIsActive());

        // Check if the ticket has the correct associations
        assertNotNull(ticket.getClient());
        assertEquals(username, ticket.getClient().getUsername());
        assertEquals(clientName, ticket.getClient().getName());
        assertEquals(clientPassword, ticket.getClient().getPassword());
        assertEquals(mmsId, ticket.getClient().getMuseumManagementSystem().getSystemId());

        assertNotNull(ticket.getMuseumManagementSystem());
        assertEquals(mmsId, ticket.getMuseumManagementSystem().getSystemId());
        assertEquals(name, ticket.getMuseumManagementSystem().getName());
        assertEquals(openTime, ticket.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, ticket.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, ticket.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, ticket.getMuseumManagementSystem().getTicketFee());
        assertEquals(mmsId, ticket.getMuseumManagementSystem().getSystemId());
    }
}
