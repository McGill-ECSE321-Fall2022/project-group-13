package ca.mcgill.ecse321.MMSBackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.dao.RoomRepository;
import ca.mcgill.ecse321.MMSBackend.dao.SpecificWeekDayRepository;
import ca.mcgill.ecse321.MMSBackend.dao.TicketRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Room.RoomType;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;

/**
 * @author Lucy Zhang (Lucy-Zh), Yu An Lu (yu-an-lu), Nazia Chowdhury (naziaC)
 *         and Samantha Perez Hoffman (samperezh)
 * 
 * The TestMuseumManagementService class tests the business logic declared in
 * MuseumManagementSystemService.
 * 
 */
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
    private static final Time OPEN_TIME_1 = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME_1 = Time.valueOf("17:00:00");
    private static final Time OPEN_TIME_2 = Time.valueOf("12:00:00");
    private static final Time CLOSE_TIME_2 = Time.valueOf("21:00:00");
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

    private static final int NONEXISTING_ID = 0;

    private static final MuseumManagementSystem MMS = new MuseumManagementSystem();
    private static final Room STORAGE_ROOM = createRoom(STORAGE_ROOM_ID, STORAGE_ROOM_NAME, STORAGE_ROOM_TYPE, MMS);
    private static final Room SMALL_ROOM = createRoom(SMALL_ROOM_ID, SMALL_ROOM_NAME, SMALL_ROOM_TYPE, MMS);
    private static final List<Room> ALL_ROOMS = Arrays.asList(STORAGE_ROOM, SMALL_ROOM);


    @BeforeEach
	public void setMockOutput()
    {

        lenient().when(mmsRepository.findMuseumManagementSystemBySystemId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(SYSTEM_ID)) {
                MuseumManagementSystem mms = new MuseumManagementSystem();
                mms.setSystemId(SYSTEM_ID);
                mms.setName(MMS_NAME);
                mms.setOpenTime(OPEN_TIME_1);
                mms.setCloseTime(CLOSE_TIME_1);
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

        initializeSpecificWeekDay(SPECIFIC_WEEK_DAY1, MONDAY, false);
        initializeSpecificWeekDay(SPECIFIC_WEEK_DAY2, TUESDAY, true);
        lenient().when(specificWeekDayRepository.findSpecificWeekDayByDayType(any())).thenAnswer((InvocationOnMock invocation) -> {
            System.out.println("invocation " + invocation.getArgument(0));
            if (invocation.getArgument(0) == null) {
                return null;
            } else if (invocation.getArgument(0).equals(MONDAY)) {
                return SPECIFIC_WEEK_DAY1;
            } else if (invocation.getArgument(0).equals(TUESDAY)) {
                return SPECIFIC_WEEK_DAY2;
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

        lenient().when(roomRepository.findAll()).thenReturn(ALL_ROOMS);

    }

     /**
     * @author Lucy Zhang (Lucy-Zh)
     */
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

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
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

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
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

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
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

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
    @Test
    public void testDeleteMuseumManagementSystem(){
    
            try {
                museumManagementSystemService.deleteMuseumManagementSystem(SYSTEM_ID);
            } catch (MuseumManagementSystemException e) {
                fail();
            }
    }

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
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

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
    @Test
    public void testSetMuseumManagementSystemName(){

            try {
                museumManagementSystemService.setMuseumManagementSystemName(SYSTEM_ID, "Not Cool Museum");
            } catch (MuseumManagementSystemException e) {
                fail();
            }
    
    }

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
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

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
    @Test
    public void testGetAllMuseumManagementSystem(){

        MuseumManagementSystem museum_1 = museumManagementSystemService.createMuseumManagementSystem();

        lenient().when(mmsRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            List<MuseumManagementSystem> mmsList = new ArrayList<MuseumManagementSystem>();
            mmsList.add(museum_1);
            return mmsList;

        });

        List<MuseumManagementSystem> testMms = museumManagementSystemService.getAllMuseumManagementSystem();

        assertNotNull(testMms);
        assertEquals(1, testMms.size());
        assertEquals(museum_1, testMms.get(0));
        
    }

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
    @Test
    public void testSetMuseumTicketPrice(){

        try {
            museumManagementSystemService.setMuseumTicketPrice(SYSTEM_ID, 10.0);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

    }

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
    @Test
    public void testSetTicketPriceNegative(){
            
        String error = "";

        try {
            museumManagementSystemService.setMuseumTicketPrice(SYSTEM_ID, -1);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertEquals("Ticket price is not valid", error);
    }

    /**
     * @author Lucy Zhang (Lucy-Zh)
     */
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

    /**
     * @author Nazia Chowdhury (naziaC)
     */
    @Test
    public void testGetMaxLoanNumberOfMms(){
        int maxLoanNumber = museumManagementSystemService.getMaxLoanNumberOfMms(SYSTEM_ID);
        assertEquals(maxLoanNumber, MMS_MAX_LOAN_NUMBER);
    }

    /**
     * @author Nazia Chowdhury (naziaC)
     */
    @Test
    public void testSetMaxLoanNumberOfMms(){
        try {
            museumManagementSystemService.setMaxLoanNumberOfMms(SYSTEM_ID, 10);
        } catch (MuseumManagementSystemException e) {
            fail();
        }
    }

    /**
     * @author Nazia Chowdhury (naziaC)
     */
    @Test
    public void testSetMaxLoanNumberOfMmsNegative(){
        String error = "";
        try {
            museumManagementSystemService.setMaxLoanNumberOfMms(SYSTEM_ID, -1);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertEquals("Maximum loan number is not valid", error);
    }

    /**
     * @author Nazia Chowdhury (naziaC)
     */
    @Test
    public void testSetMaxLoanNumberOfMmsNull(){

        String error = "";

        try {
            museumManagementSystemService.setMaxLoanNumberOfMms(-1, 13);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }
        assertEquals("Museum Management System does not exist", error);
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     */
    @Test
    public void testSetOpeningHours(){
        MuseumManagementSystem museumManagementSystem = museumManagementSystemService.setOpeningHours(OPEN_TIME_2, CLOSE_TIME_2, SYSTEM_ID);

        assertNotNull(museumManagementSystem);
        assert(museumManagementSystem.getOpenTime().equals(OPEN_TIME_2));
        assert(museumManagementSystem.getCloseTime().equals(CLOSE_TIME_2));
        verify(mmsRepository, times(1)).save(museumManagementSystem);
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     */
    @Test 
    public void testSetOpeningHoursWithInvalidMMS(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            museumManagementSystemService.setOpeningHours(OPEN_TIME_1, OPEN_TIME_2, 0);
        });

        assertEquals("Museum Management System does not exist.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(mmsRepository, times(1)).findMuseumManagementSystemBySystemId(0);
        verify(mmsRepository, never()).save(any(MuseumManagementSystem.class));
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     */
    @Test 
    public void testSetOpeningHoursWithNullTimes(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            museumManagementSystemService.setOpeningHours(null, null , SYSTEM_ID);
        });

        assertEquals("Opening and closing times cannot be null.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(mmsRepository, never()).save(any(MuseumManagementSystem.class));
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     */
    @Test 
    public void testSetOpeningHoursWithInvalidTimes(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            museumManagementSystemService.setOpeningHours(CLOSE_TIME_2, OPEN_TIME_2 , SYSTEM_ID);
        });

        assertEquals("Opening time cannot be after closing time.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(mmsRepository, never()).save(any(MuseumManagementSystem.class));
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     */
    @Test
    public void testGetOpeningHours(){
        List<Time> openingHours = museumManagementSystemService.getOpeningHours(SYSTEM_ID);
    
        assertNotNull(openingHours);
        assertEquals(OPEN_TIME_1, openingHours.get(0));
        assertEquals(CLOSE_TIME_1, openingHours.get(1));
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     */
    @Test
    public void testGetOpeningHoursWithInvalidMMS(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            museumManagementSystemService.getOpeningHours(0);
        });

        assertEquals("Museum Management System does not exist.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(mmsRepository, times(1)).findMuseumManagementSystemBySystemId(0);
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     */
    @Test
    public void testGetSpecificWeekDayByDayType(){
        SpecificWeekDay specificWeekDay =  museumManagementSystemService.getSpecificWeekDayByDayType(MONDAY);
        assertNotNull(specificWeekDay);
        assertEquals(MONDAY, specificWeekDay.getDayType());
        assertEquals(false, specificWeekDay.getIsClosed());
        verify(specificWeekDayRepository, times(1)).findSpecificWeekDayByDayType(MONDAY);
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     */
    @Test
    public void testGetSpecificWeekDayByDayTypeWithNullDay(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            museumManagementSystemService.getSpecificWeekDayByDayType(null);
        });

        assertEquals("Specific Week Day does not exist.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(specificWeekDayRepository, never()).findAll();
    }
    
    /**
     * @author Yu An Lu (yu-an-lu)
     */
    @Test 
    public void testGetExistingRoom(){
        Room room = null;
        try {
            room = museumManagementSystemService.getRoom(STORAGE_ROOM_ID);
        } catch (MuseumManagementSystemException e) {
            fail();
        }

        assertNotNull(room);
    }
    
    /**
     * @author Yu An Lu (yu-an-lu)
     */
    @Test
    public void testGetNonExistingRoom(){
        String error = null;

        Room room = null;
        try {
            room = museumManagementSystemService.getRoom(NONEXISTING_ID);
        } catch (MuseumManagementSystemException e) {
            error = e.getMessage();
        }

        assertNull(room);
        assertEquals("Room does not exist", error);
    }

    /**
     * @author Yu An Lu (yu-an-lu)
     */
    @Test
    public void testGetAllRooms(){
        List<Room> rooms = museumManagementSystemService.getAllRooms();
        assertEquals(ALL_ROOMS, rooms);
    }

    /**
     * @author Yu An Lu (yu-an-lu)
     */
    @Test
    public void testGetAllRoomsByType(){
        List<Room> room = museumManagementSystemService.getAllRoomsByType(STORAGE_ROOM_TYPE);
        assertEquals(1, room.size());
        assertEquals(STORAGE_ROOM, room.get(0));
    }

    public void checkMuseumManagementSystemValidness(MuseumManagementSystem museumManagementSystem){
        assertEquals("Museum Management System Default Name", museumManagementSystem.getName());
        assertEquals(Time.valueOf("9:00:00"), museumManagementSystem.getOpenTime());
        assertEquals(Time.valueOf("17:00:00"), museumManagementSystem.getCloseTime());
        assertEquals(5, museumManagementSystem.getMaxLoanNumber());
        assertEquals(0.00, museumManagementSystem.getTicketFee());
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     * @param specificWeekDay
     * @param dayType
     * @param isClosed
     */
    public void initializeSpecificWeekDay(SpecificWeekDay specificWeekDay, DayType dayType, boolean isClosed){
        specificWeekDay.setDayType(dayType);
        specificWeekDay.setIsClosed(isClosed);
        specificWeekDay.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(SYSTEM_ID));
    }

    /**
     * @author Yu An Lu (yu-an-lu)
     * @param id
     * @param name
     * @param roomType
     * @param mms
     * @return the created room
     */
    private static Room createRoom(int id, String name, RoomType roomType, MuseumManagementSystem mms) {
        Room room = new Room();
        room.setRoomId(id);
        room.setName(name);
        room.setType(roomType);
        room.setMuseumManagementSystem(mms);
        return room;
    }
}
