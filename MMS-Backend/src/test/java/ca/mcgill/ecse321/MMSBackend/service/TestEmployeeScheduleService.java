package ca.mcgill.ecse321.MMSBackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MMSBackend.dao.ShiftRepository;
import ca.mcgill.ecse321.MMSBackend.dao.SpecificWeekDayRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Shift;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;

/**
 * @author Samantha Perez Hoffman (samperezh)
 * 
 * The TestEmployeeScheduleService class tests the business logic declared in
 * EmployeeScheduleService.
 * 
 */
@ExtendWith(MockitoExtension.class)
public class TestEmployeeScheduleService {

    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private SpecificWeekDayRepository specificWeekDayRepository;

    @InjectMocks
    private EmployeeScheduleService service;

    private static final SpecificWeekDay SPECIFIC_WEEK_DAY1 = new SpecificWeekDay();
    private static final SpecificWeekDay SPECIFIC_WEEK_DAY2 = new SpecificWeekDay();
    private static final DayType MONDAY = DayType.Monday;
    private static final DayType TUESDAY = DayType.Tuesday;
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final Time START_TIME = Time.valueOf("10:00:00");
    private static final Time END_TIME = Time.valueOf("12:00:00");

    private static final int VALID_SHIFT_ID = 1;
    private static final String USERNAME1 = "username1";
    private static final String USERNAME2 = "username2";

    private MuseumManagementSystem MMS = new MuseumManagementSystem();
    private Employee EMPLOYEE1 = new Employee();
    private Employee EMPLOYEE2 = new Employee();

    private Shift SHIFT1 = new Shift();


    @BeforeEach
    public void setMockOutput() {
        SPECIFIC_WEEK_DAY1.setIsClosed(false);
        SPECIFIC_WEEK_DAY1.setDayType(MONDAY);
        SPECIFIC_WEEK_DAY2.setIsClosed(false);
        SPECIFIC_WEEK_DAY2.setDayType(TUESDAY);
        MMS.setOpenTime(OPEN_TIME);
        MMS.setCloseTime(CLOSE_TIME);
        EMPLOYEE1 = initializeEmployee(USERNAME1, MMS);
        EMPLOYEE2 = initializeEmployee(USERNAME2, MMS);
        SHIFT1 = initializeShift(VALID_SHIFT_ID, EMPLOYEE1, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);

        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            //Shift existingShift = initializeShift(VALID_SHIFT_ID, EMPLOYEE1, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(SHIFT1);
            return shifts;
        });
        lenient().when(shiftRepository.findShiftByShiftId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VALID_SHIFT_ID)) {
                return SHIFT1;
            } else {
                return null;
            }
        });
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(shiftRepository.save(any(Shift.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(specificWeekDayRepository.save(any(SpecificWeekDay.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateShift() {
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            return new ArrayList<Shift>();
        });
        Shift shift = null;
        shift = service.createShift(EMPLOYEE1, SPECIFIC_WEEK_DAY1, OPEN_TIME, CLOSE_TIME);

        assertNotNull(shift);
        assertEquals(EMPLOYEE1, shift.getEmployee());
        assertEquals(SPECIFIC_WEEK_DAY1, shift.getDayOfTheWeek());
        assertEquals(OPEN_TIME, shift.getStartTime());
        assertEquals(CLOSE_TIME, shift.getEndTime());
        verify(shiftRepository, times(1)).save(shift);

    }

    @Test
    public void testCreateShiftWithNullStartTime() {
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.createShift(EMPLOYEE1, SPECIFIC_WEEK_DAY1, null, CLOSE_TIME);
        });

        assertEquals("Null values are not allowed!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testCreateShiftWithInvalidStartEndTime() {
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.createShift(EMPLOYEE1, SPECIFIC_WEEK_DAY1, CLOSE_TIME, OPEN_TIME);
        });

        assertEquals("Start time must be before end time!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testCreateShiftWithClosedSpecificWeekDay() {
        SPECIFIC_WEEK_DAY1.setIsClosed(true);
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.createShift(EMPLOYEE1, SPECIFIC_WEEK_DAY1, OPEN_TIME, CLOSE_TIME);
        });

        assertEquals("This day is closed, cannot create shift", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testCreateShiftThatOverlapsWithExistingShift() {
        // existing shift Monday 10:00-12:00 overlaps with new shift Monday 9:00-17:00
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.createShift(EMPLOYEE1, SPECIFIC_WEEK_DAY1, OPEN_TIME, CLOSE_TIME);
        });

        assertEquals("Employee already has a shift that overlaps with this new shift", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testDeleteShift() {
        service.deleteShift(VALID_SHIFT_ID);
        verify(shiftRepository, times(1)).deleteById(VALID_SHIFT_ID);
    }

    @Test
    public void testDeleteAllShiftsForDay() {
        // create shift on that day
        service.deleteAllShiftsForDay(MONDAY);
        verify(shiftRepository, times(1)).findAll();
        verify(shiftRepository, times(1)).deleteById(VALID_SHIFT_ID); 
    }

    @Test
    public void testDeleteAllShiftsForNullDay() {
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.deleteAllShiftsForDay(null);
        });

        assertEquals("Null values are not allowed!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testGetAllShifts(){
        List<Shift> allShifts = service.getAllShifts();

        assertNotNull(allShifts);
        assertEquals(1, allShifts.size());
        assertEquals(SHIFT1, allShifts.get(0));
        verify(shiftRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllShiftsForEmployee(){
       Shift shift2 = initializeShift(VALID_SHIFT_ID+1, EMPLOYEE2, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(SHIFT1);
            shifts.add(shift2);
            return shifts;
        });
        List<Shift> allShifts = service.getAllShiftsForEmployee(EMPLOYEE1);

        assertNotNull(allShifts);
        assertEquals(1, allShifts.size());
        assertEquals(SHIFT1, allShifts.get(0));
        verify(shiftRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllShiftsForNullEmployee(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.getAllShiftsForEmployee(null);
        });

        assertEquals("Employee does not exist!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(shiftRepository, never()).findAll();
    }

    @Test
    public void testGetAllShiftsForDay(){
        Shift shift2 = initializeShift(VALID_SHIFT_ID+1, EMPLOYEE2, SPECIFIC_WEEK_DAY2, START_TIME, END_TIME);
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(SHIFT1);
            shifts.add(shift2);
            return shifts;
        });
        List<Shift> allShifts = service.getAllShiftsForDay(MONDAY);

        assertNotNull(allShifts);
        assertEquals(1, allShifts.size());
        assertEquals(SHIFT1, allShifts.get(0));
        verify(shiftRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllShiftsForNullDay(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.getAllShiftsForDay(null);
        });

        assertEquals("Null values are not allowed!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).findAll();
    }

    @Test
    public void testUpdateShiftStartEndTime(){
        Shift shift = service.updateShiftStartEndTime(VALID_SHIFT_ID, OPEN_TIME, CLOSE_TIME);

        assertNotNull(shift);
        assertEquals(EMPLOYEE1, shift.getEmployee());
        assertEquals(SPECIFIC_WEEK_DAY1, shift.getDayOfTheWeek());
        assertEquals(OPEN_TIME, shift.getStartTime());
        assertEquals(CLOSE_TIME, shift.getEndTime());
        verify(shiftRepository, times(1)).save(shift);
    }

    @Test
    public void testUpdateShiftStartEndTimeWithInvalidShift(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateShiftStartEndTime(0, OPEN_TIME, CLOSE_TIME);
        });

        assertEquals("No shift with given id exists.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test 
    public void testUpdateShiftStartEndTimeWithNullStartEndTimes(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateShiftStartEndTime(VALID_SHIFT_ID, null, null);
        });

        assertEquals("Null values are not allowed!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testUpdateShiftStartEndTimeWithInvalidStartEndTimes(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateShiftStartEndTime(VALID_SHIFT_ID, CLOSE_TIME, OPEN_TIME);
        });

        assertEquals("Start time cannot be after end time.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testUpdateShiftStartEndTimeThatOverlapsWithExistingShift(){
        //set up overlapping shifts 
        Shift shift1 = initializeShift(VALID_SHIFT_ID, EMPLOYEE1, SPECIFIC_WEEK_DAY1, Time.valueOf("13:00:00"), Time.valueOf("14:00:00")); //old shift
        Shift shift2 = initializeShift(VALID_SHIFT_ID+1, EMPLOYEE1, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME); //overlaps with newShift 
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(shift1);
            shifts.add(shift2);
            return shifts;
        });
        lenient().when(shiftRepository.findShiftByShiftId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VALID_SHIFT_ID)) {
                return shift1;
            } else {
                return null;
            }
        });

        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateShiftStartEndTime(VALID_SHIFT_ID, OPEN_TIME, CLOSE_TIME);
        });

        assertEquals("Employee already has a shift that overlaps with this new shift", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testUpdateShiftDay(){
        Shift shift = service.updateShiftDay(VALID_SHIFT_ID, SPECIFIC_WEEK_DAY2);

        assertNotNull(shift);
        assertEquals(VALID_SHIFT_ID, shift.getShiftId());
        assertEquals(EMPLOYEE1, shift.getEmployee());
        assertEquals(SPECIFIC_WEEK_DAY2, shift.getDayOfTheWeek());
        assertEquals(START_TIME, shift.getStartTime());
        assertEquals(END_TIME, shift.getEndTime());
        verify(shiftRepository, times(1)).save(shift);
    }

    @Test 
    public void testUpdateShiftDayWithInvalidShift(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateShiftDay(0, SPECIFIC_WEEK_DAY2);
        });

        assertEquals("No shift with given id exists.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test 
    public void testUpdateShiftDayWithNullDay(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateShiftDay(VALID_SHIFT_ID, null);
        });

        assertEquals("Null values are not allowed!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testUpdateShiftDayWithClosedSpecificWeekDay(){
        SPECIFIC_WEEK_DAY2.setIsClosed(true);
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateShiftDay(VALID_SHIFT_ID, SPECIFIC_WEEK_DAY2);
        });

        assertEquals("This day is closed, cannot update shift", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    public void testUpdateShiftDayThatOverlapsWithExistingShift(){
        //set up overlapping shifts 
        Shift shift2 = initializeShift(VALID_SHIFT_ID+1, EMPLOYEE1, SPECIFIC_WEEK_DAY2, OPEN_TIME, CLOSE_TIME); //overlaps with newShift 
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(SHIFT1);
            shifts.add(shift2);
            return shifts;
        });

        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateShiftDay(VALID_SHIFT_ID, SPECIFIC_WEEK_DAY2);
        });

        assertEquals("Employee already has a shift that overlaps with this new shift", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test 
    public void testUpdateSpecificWeekDayStatus(){
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            return new ArrayList<Shift>();
        });
        SpecificWeekDay specificWeekDay = service.updateSpecificWeekDayStatus(SPECIFIC_WEEK_DAY1, true);

        assertNotNull(specificWeekDay);
        assertEquals(SPECIFIC_WEEK_DAY1.getDayType(), specificWeekDay.getDayType());
        assertEquals(true, specificWeekDay.getIsClosed());
    }

    @Test 
    public void testUpdateSpecificWeekDayStatusWithNullDay(){
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateSpecificWeekDayStatus(null, false);
        });

        assertEquals("No specificWeekDay exists.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(specificWeekDayRepository, never()).save(any(SpecificWeekDay.class));
    }

    @Test
    public void testUpdateSpecificWeekDayStatusWithExistingShifts() {
        MuseumManagementSystemException exception = assertThrows(MuseumManagementSystemException.class, () -> {
            service.updateSpecificWeekDayStatus(SPECIFIC_WEEK_DAY1, true);
        });  

        assertEquals("Cannot close day with that has scheduled shifts.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(specificWeekDayRepository, never()).save(any(SpecificWeekDay.class)); 

    }

    private Shift initializeShift(int shiftId, Employee employee, SpecificWeekDay specificWeekDay, Time startTime, Time endTime){
        Shift shift = new Shift();
        if(Shift.hasWithShiftId(shiftId)){
            shift = Shift.getWithShiftId(shiftId);
        } else {
           shift.setShiftId(shiftId);
        }
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
        shift.setDayOfTheWeek(specificWeekDay);
        shift.setEmployee(employee);
        return shift;
    }

    private Employee initializeEmployee(String username, MuseumManagementSystem mms){
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setMuseumManagementSystem(mms);
        return employee;
    }
}
