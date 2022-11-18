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
import java.util.List;

import ca.mcgill.ecse321.MMSBackend.model.*;
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
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
import ca.mcgill.ecse321.MMSBackend.dao.SpecificWeekDayRepository;
import ca.mcgill.ecse321.MMSBackend.dao.TicketRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;

@ExtendWith(MockitoExtension.class)
public class TestMuseumManagementService {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MuseumManagementSystemRepository mmsRepository;

    @Mock
    private SpecificWeekDayRepository specificWeekDayRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    MuseumManagementSystemService museumManagementSystemService;

    private static final int TICKET_ID = 12345;
    private static final double TICKET_FEE = 69.69;
    private static final boolean IS_ACTIVE = true;

    private static final String CLIENT_USERNAME = "TaylorSwift13";
    private static final String CLIENT_NAME = "Taylor Swift";
    private static final String CLIENT_PASSWORD = "ReputationBestAlbum";
    private static final int CLIENT_LOAN_NUMBER = 0;

    private static final int SYSTEM_ID = 54321;
    private static final String MMS_NAME = "Cool Museum";
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final int MMS_MAX_LOAN_NUMBER = 5;
    private static final double MMS_TICKET_FEE = 96.96;

    private static final SpecificWeekDay SPECIFIC_WEEK_DAY1 = new SpecificWeekDay();
    private static final SpecificWeekDay SPECIFIC_WEEK_DAY2 = new SpecificWeekDay();
    private static final DayType MONDAY = DayType.Monday;
    private static final DayType TUESDAY = DayType.Tuesday;

    private static final int STORAGE_ROOM_ID = 6;
    private static final String STORAGE_ROOM_NAME = "Storage Room";
    private static final RoomType STORAGE_ROOM_TYPE = RoomType.Storage;
    private static final int SMALL_ROOM_ID = 7;
    private static final String SMALL_ROOM_NAME = "Small Room 1";
    private static final RoomType SMALL_ROOM_TYPE = RoomType.Small;

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

        SPECIFIC_WEEK_DAY1.setIsClosed(false);
        SPECIFIC_WEEK_DAY1.setDayType(MONDAY);
        SPECIFIC_WEEK_DAY2.setIsClosed(false);
        SPECIFIC_WEEK_DAY2.setDayType(TUESDAY);

        lenient().when(roomRepository.findRoomByRoomId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
                if (invocation.getArgument(0).equals(STORAGE_ROOM_ID)) {
                    Room room = new Room();
                    room.setRoomId(STORAGE_ROOM_ID);
                    room.setName(STORAGE_ROOM_NAME);
                    room.setType(STORAGE_ROOM_TYPE);
                    room.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
                    return room;
                } else if (invocation.getArgument(0).equals(SMALL_ROOM_ID)) {
                    Room room = new Room();
                    room.setRoomId(SMALL_ROOM_ID);
                    room.setName(SMALL_ROOM_NAME);
                    room.setType(SMALL_ROOM_TYPE);
                    room.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
                    return room;
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
        lenient().when(specificWeekDayRepository.save(any(SpecificWeekDay.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(roomRepository.save(any(Room.class))).thenAnswer(returnParameterAsAnswer);

    }

    //this isn't working yet
    @Test
    public void testCreateMuseumManagementSystem(){

        assertEquals(0, museumManagementSystemService.getAllMuseumManagementSystem().size());

        MuseumManagementSystem museumManagementSystem = null;

        try {
            museumManagementSystem = museumManagementSystemService.createMuseumManagementSystem();
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(museumManagementSystem);
        checkMuseumManagementSystemValidness(museumManagementSystem);

    }

    @Test
    public void testGetMaxLoanNumberOfMms(){
        int maxLoanNumber = museumManagementSystemService.getMaxLoanNumberOfMms(SYSTEM_ID);
        assertEquals(maxLoanNumber, MMS_MAX_LOAN_NUMBER);
    }
    
    @Test
    public void testCreateMuseumManagementSystemWithExistingSystemId() {

        lenient().when(mmsRepository.count()).thenAnswer((InvocationOnMock invocation) -> {
            return (long)1;
        });

        MuseumManagementSystem newMuseumManagementSystem = null;

        String error = "";

        try {
            newMuseumManagementSystem = museumManagementSystemService.createMuseumManagementSystem();
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Museum Management System already exists", error);

    }

    @Test
    public void testGetMuseumManagementSystem() {

        MuseumManagementSystem museumManagementSystem = null;

        try {
            museumManagementSystem = museumManagementSystemService.getMuseumManagementSystem(SYSTEM_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(museumManagementSystem);

    }

    @Test
    public void testGetMuseumManagementSystemNull(){

        MuseumManagementSystem museumManagementSystem = null;

        String error = "";

        try {
            museumManagementSystem = museumManagementSystemService.getMuseumManagementSystem(-1);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(museumManagementSystem);
        assertEquals("Museum Management System does not exist", error);

    }

    @Test
    public void testDeleteMuseumManagementSystem(){
    
            try {
                museumManagementSystemService.deleteMuseumManagementSystem(SYSTEM_ID);
            } catch (MuseumManagementSystemException e) {
                fail();
            }
    }

    @Test
    public void testDeleteMuseumManagementSystemNull(){

        String error = "";

        try {
            museumManagementSystemService.deleteMuseumManagementSystem(-1);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Museum Management System does not exist", error);

    }

    @Test
    public void testSetMuseumManagementSystemName(){

            try {
                museumManagementSystemService.setMuseumManagementSystemName(SYSTEM_ID, "Not Cool Museum");
            } catch (MuseumManagementSystemException e) {
                fail();
            }
    
    }

    @Test
    public void testSetMuseumManagementSystemNameNull(){

        String error = "";

        try {
            museumManagementSystemService.setMuseumManagementSystemName(-1, "Not Cool Museum");
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Museum Management System does not exist", error);

    }

    @Test
    public void testGetAllMuseumManagementSystem(){

        
    }

    @Test
    public void testSetMuseumTicketPrice(){

        try {
            museumManagementSystemService.setMuseumTicketPrice(SYSTEM_ID, 10.0);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

    }

    @Test   
    public void testSetMuseumTicketPriceNull(){

        String error = "";

        try {
            museumManagementSystemService.setMuseumTicketPrice(-1, 10.0);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Museum Management System does not exist", error);

    }

    public void checkMuseumManagementSystemValidness(MuseumManagementSystem museumManagementSystem){

        assertEquals("Museum Management System Default Name", museumManagementSystem.getName());
        assertEquals(Time.valueOf("9:00:00"), museumManagementSystem.getOpenTime());
        assertEquals(Time.valueOf("17:00:00"), museumManagementSystem.getCloseTime());
        assertEquals(5, museumManagementSystem.getMaxLoanNumber());
        assertEquals(0.00, museumManagementSystem.getTicketFee());

    }
}
