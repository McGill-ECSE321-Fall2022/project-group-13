package ca.mcgill.ecse321.MMSBackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.TicketRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

/**
 * @author Lucy Zhang (Lucy-Zh)
 * 
 * The TestTicketService class tests the business logic declared in TicketService.
 * 
 */
@ExtendWith(MockitoExtension.class)
public class TestTicketService {
    
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MuseumManagementSystemRepository mmsRepository;

    @InjectMocks
    TicketService ticketService;

    private static final int TICKET_ID = 12345;
    private static final double TICKET_FEE = 69.69;
    private static final boolean IS_ACTIVE = true;

    private static final String CLIENT_USERNAME_A = "TaylorSwift13";
    private static final String CLIENT_USERNAME_B = "KarlieKloss";
    private static final String CLIENT_USERNAME_C = "SelenaGomez";
    private static final String CLIENT_NAME = "Taylor Swift";
    private static final String CLIENT_PASSWORD = "ReputationBestAlbum";
    private static final int CLIENT_LOAN_NUMBER = 0;

    private static final int SYSTEM_ID = 54321;
    private static final String MMS_NAME = "Best Museum of All Time";
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final int MMS_MAX_LOAN_NUMBER = 5;
    private static final double MMS_TICKET_FEE = 69.69;

    @BeforeEach
	public void setMockOutput()
    {
        lenient().when(mmsRepository.findMuseumManagementSystemBySystemId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(SYSTEM_ID)) {
                MuseumManagementSystem mms = new MuseumManagementSystem();
                mms.setSystemId(SYSTEM_ID);
                mms.setName(MMS_NAME);
                mms.setOpenTime(OPEN_TIME);
                mms.setCloseTime(CLOSE_TIME);
                mms.setMaxLoanNumber(MMS_MAX_LOAN_NUMBER);
                mms.setTicketFee(MMS_TICKET_FEE);
                return mms;
            } else {
                return null;
            }
        });

        lenient().when(clientRepository.findClientByUsername(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(CLIENT_USERNAME_A)) {
                return client(CLIENT_USERNAME_A, CLIENT_NAME, CLIENT_PASSWORD, CLIENT_LOAN_NUMBER, mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
            }
            else if (invocation.getArgument(0).equals(CLIENT_USERNAME_B)){
                return client(CLIENT_USERNAME_B,CLIENT_NAME,CLIENT_PASSWORD,CLIENT_LOAN_NUMBER,mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
            }
            else if (invocation.getArgument(0).equals(CLIENT_USERNAME_C)){
                return client(CLIENT_USERNAME_C,CLIENT_NAME,CLIENT_PASSWORD,CLIENT_LOAN_NUMBER,mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
            }else {
                return null;
            }
    });

        lenient().when(ticketRepository.findTicketByTicketId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(TICKET_ID)) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(TICKET_ID);
                ticket.setClient(clientRepository.findClientByUsername(CLIENT_USERNAME_C));
                ticket.setFee(TICKET_FEE);
                ticket.setIsActive(IS_ACTIVE);
                ticket.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
                return ticket;
            } else {
                return null;
            }
        });

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};

        lenient().when(ticketRepository.save(any(Ticket.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(mmsRepository.save(any(MuseumManagementSystem.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(clientRepository.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
    }


    @Test
    public void testCreateTicket() {
        assertEquals(0, ticketService.getAllTickets().size());
        Ticket ticket = null;

        try {
            ticket = ticketService.createTicket(CLIENT_USERNAME_C);
        } catch (MuseumManagementSystemException e) {
            fail();
        }
        assertNotNull(ticket);
        checkTicketValidness(ticket);
    }

    @Test
    public void testCreateTicketNullClient(){
        String error = "";
        Ticket ticket = null;

        try {
            ticket = ticketService.createTicket("");
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertNull(ticket);
        assertEquals("This username is invalid", error);
    }


    @Test
    public void testGetTicket(){

        Ticket ticket = null;
        try {
            ticket = ticketService.getTicket(TICKET_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }
        assertNotNull(ticket);
    }

    @Test
    public void testGetTicketNullTicket(){

        String error = "";
        Ticket ticket = null;

        try {
            ticket = ticketService.getTicket(-1);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(ticket);
        assertEquals("Ticket does not exist", error);

    }

    @Test
    public void testDeleteTicket(){
        try {
            ticketService.deleteTicket(TICKET_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }
    }

    @Test
    public void testDeleteTicketNullTicket(){

        String error = "";

        try {
            ticketService.deleteTicket(-1);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Ticket does not exist", error);
    }

    @Test
    public void testGetAllTickets(){

        Ticket ticket_1 = ticketService.createTicket(CLIENT_USERNAME_C);

        lenient().when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            List<Ticket> tickets = new ArrayList<Ticket>();
            tickets.add(ticket_1);
            return tickets;

        });

        List<Ticket> testTickets = ticketService.getAllTickets();

        assertNotNull(testTickets);
        assertEquals(1, testTickets.size());
        assertEquals(ticket_1, testTickets.get(0));
    }

    @Test
    public void testGetAllTicketsByClient(){

        Client client1 = client(CLIENT_USERNAME_A, CLIENT_NAME, CLIENT_PASSWORD, CLIENT_LOAN_NUMBER, mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
        Client client2 = client(CLIENT_USERNAME_B, CLIENT_NAME, CLIENT_PASSWORD, CLIENT_LOAN_NUMBER, mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));

        Ticket ticket_1 = new Ticket();
        setTicketAttributes(ticket_1, client1);
        Ticket ticket_2 = new Ticket();
        setTicketAttributes(ticket_2, client2);
        Ticket ticket_3 = new Ticket();
        setTicketAttributes(ticket_3, client1);

        lenient().when(ticketRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            List<Ticket> tickets = new ArrayList<Ticket>();
            tickets.add(ticket_1);
            tickets.add(ticket_2);
            tickets.add(ticket_3);
            return tickets;
        });

        List<Ticket> taylorSwiftTickets = ticketService.getAllTicketsByClient(client1);

        assertNotNull(taylorSwiftTickets);
        assertEquals(2, taylorSwiftTickets.size());
        assertEquals(ticket_1, taylorSwiftTickets.get(0));

    }

    @Test
    public void testSetTicketStatus(){
            
            Ticket ticket = ticketService.getTicket(TICKET_ID);
            boolean status = !(ticket.getIsActive());
    
            try {
                ticketService.setTicketStatus(ticket, status);
            } catch (MuseumManagementSystemException e) {
                fail();
            }
    
            assertNotNull(ticket);
            assertEquals(status, ticket.getIsActive());
    }

    @Test
    public void testSetTicketStatusNullTicket(){

        String error = "";

        try {
            ticketService.setTicketStatus(null, false);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Ticket does not exist", error);
    }

    /**
     * @author Lucy Zhang (Lucy-Zh)
     * 
     * Helper method to check if the ticket is valid
     * 
     */
    private void checkTicketValidness(Ticket ticket){
        assertEquals(TICKET_FEE, ticket.getFee());
        assertEquals(IS_ACTIVE, ticket.getIsActive());
        assertEquals(CLIENT_USERNAME_C, ticket.getClient().getUsername());
    }

    /**
     * @author Lucy Zhang (Lucy-Zh)
     * 
     * Helper method to set the attributes of a ticket
     * @param ticket
     * @param client
     * 
     */
    private void setTicketAttributes(Ticket ticket, Client client){
        ticket.setFee(TICKET_FEE);
        ticket.setIsActive(IS_ACTIVE);
        ticket.setClient(client);
        ticket.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
    }

     /**
     * @author Lucy Zhang (Lucy-Zh)
     * 
     * Helper method to create a client
     * @param username
     * @param name
     * @param password
     * @param loanNumber
     * @param mms
     * 
     */
    private Client client(String username, String name, String password, int loanNumber, MuseumManagementSystem mms){
        Client client = new Client(); 
        client.setUsername(username); 
        client.setName(name); 
        client.setPassword(password); 
        client.setCurrentLoanNumber(loanNumber); 
        client.setMuseumManagementSystem(mms);
        return client;
    }

}
