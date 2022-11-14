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

    private static final String CLIENT_USERNAME = "TaylorSwift13";
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

        lenient().when(clientRepository.findClientByUsername(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(CLIENT_USERNAME)) {
                Client client = new Client();
                client.setUsername(CLIENT_USERNAME);
                client.setName(CLIENT_NAME);
                client.setPassword(CLIENT_PASSWORD);
                client.setCurrentLoanNumber(CLIENT_LOAN_NUMBER);
                client.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
                return client;
            } else {
                return null;
            }
        });

        lenient().when(ticketRepository.findTicketByTicketId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(TICKET_ID)) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(TICKET_ID);
                ticket.setClient(clientRepository.findClientByUsername(CLIENT_USERNAME));
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
            ticket = ticketService.createTicket(CLIENT_USERNAME);
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

        Ticket ticket = null;

        try {
            ticketService.deleteTicket(TICKET_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNull(ticket);
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

    //this doesn't work yet
    @Test
    public void testSetTicketStatus(){
            
            Ticket ticket = ticketService.getTicket(TICKET_ID);
            boolean status = !(ticket.getIsActive());
    
            try {
                ticketService.setTicketStatus(TICKET_ID, status);
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
            ticketService.setTicketStatus(-1, false);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Ticket does not exist", error);
    }

    public void checkTicketValidness(Ticket ticket){
        assertEquals(TICKET_FEE, ticket.getFee());
        assertEquals(IS_ACTIVE, ticket.getIsActive());
        assertEquals(CLIENT_USERNAME, ticket.getClient().getUsername());
        assertEquals(SYSTEM_ID, ticket.getMuseumManagementSystem().getSystemId());
    }

}
