package ca.mcgill.ecse321.MMSBackend.repository;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ClientRepositoryTests {
    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @Autowired
    private ClientRepository clientRepository;
    
    @AfterEach
    public void clearDatabase() {
        // Delete the client first to avoid violating not-null constraints
        clientRepository.deleteAll(); 
        mmsRepository.deleteAll();
    }

    @Test
    public void testClientRepository(){

        // Creating a museum management system using a plain constructor
        MuseumManagementSystem mms = new MuseumManagementSystem();
        String museumName = "Marwan's MMS";
        Time openTime = Time.valueOf("9:00:00");
        Time closeTime = Time.valueOf("17:00:00");
        int maxLoanNumber = 5;
        double ticketFee = 13.25;

        // Setting the museum management system's attributes using setters
        mms.setName(museumName);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);
    
        // Saving the museum anagement system to the db 
        mmsRepository.save(mms);

        // Creating a employees
        Client client = new Client(); 
        String username = "XoeyZhang"; 
        String name = "Xoey Zhang"; 
        String password = "ecse223";
        
        client.setUsername(username); 
        client.setName(name);
        client.setPassword(password);
        client.setMuseumManagementSystem(mms); 

        // Saving the manager to the database 
        clientRepository.save(client); 

        //  Getting ids of Manager and mms 
        String clientUsername = client.getUsername(); 
        int museumID = mms.getSystemId(); 

        // Make the variables null 
        mms = null; 
        client = null; 

        // Fetching information from the database 
        client = clientRepository.findClientByUsername(clientUsername);

        // Checking for existence of manager and valid username 
        assertNotNull(client);
        assertEquals(clientUsername, client.getUsername()); 
        
        //  Checking for proper connection between museum and manager 
        assertNotNull(client.getMuseumManagementSystem());
        assertEquals(museumID, client.getMuseumManagementSystem().getSystemId());
        assertEquals(museumName, client.getMuseumManagementSystem().getName());
        assertEquals(openTime, client.getMuseumManagementSystem().getOpenTime());
        assertEquals(closeTime, client.getMuseumManagementSystem().getCloseTime());
        assertEquals(maxLoanNumber, client.getMuseumManagementSystem().getMaxLoanNumber());
        assertEquals(ticketFee, client.getMuseumManagementSystem().getTicketFee());
    }
}
