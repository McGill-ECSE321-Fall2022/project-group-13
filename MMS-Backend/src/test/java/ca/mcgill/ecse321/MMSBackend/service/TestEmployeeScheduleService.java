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
    Employee EMPLOYEE1 = new Employee();
    Employee EMPLOYEE2 = new Employee();

    @BeforeEach
    public void setMockOutput() {
        SPECIFIC_WEEK_DAY1.setIsClosed(false);
        SPECIFIC_WEEK_DAY1.setDayType(MONDAY);
        SPECIFIC_WEEK_DAY2.setIsClosed(false);
        SPECIFIC_WEEK_DAY2.setDayType(TUESDAY);
        EMPLOYEE1.setMuseumManagementSystem(MMS);
        MMS.setOpenTime(OPEN_TIME);
        MMS.setCloseTime(CLOSE_TIME);
        EMPLOYEE1 = initializeEmployee(USERNAME1, MMS);
        EMPLOYEE2 = initializeEmployee(USERNAME2, MMS);

        // lenient().when(shiftRepository.findShiftByShiftId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
        //     if (invocation.getArgument(0).equals(VALID_SHIFT_ID)) {
        //         return SHIFT1;
        //     } else {
        //         return null;
        //     }
        // });

        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(shiftRepository.save(any(Shift.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(specificWeekDayRepository.save(any(SpecificWeekDay.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateShift() {
        Shift shift = null;
        shift = service.createShift(EMPLOYEE1, SPECIFIC_WEEK_DAY1, OPEN_TIME, CLOSE_TIME);

        assertNotNull(shift);
        assertEquals(EMPLOYEE1, shift.getEmployee());
        assertEquals(SPECIFIC_WEEK_DAY1, shift.getDayOfTheWeek());
        assertEquals(OPEN_TIME, shift.getStartTime());
        assertEquals(CLOSE_TIME, shift.getEndTime());

        // assert save was only called once
        verify(shiftRepository, times(1)).save(any(Shift.class));

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
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            Shift existingShift = initializeShift(VALID_SHIFT_ID, EMPLOYEE1, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(existingShift);
            return shifts;
        });
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
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            System.out.println("initialize shift");
            Shift existingShift = initializeShift(VALID_SHIFT_ID, EMPLOYEE1, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(existingShift);
            return shifts;
        });
        System.out.println("deleteAllShiftsForDay");
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
        Shift existingShift = initializeShift(VALID_SHIFT_ID, EMPLOYEE1, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(existingShift);
            return shifts;
        });
        List<Shift> allShifts = service.getAllShifts();
        assertEquals(1, allShifts.size());
        assertEquals(existingShift, allShifts.get(0));
        verify(shiftRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllShiftsForEmployee(){
        Shift shift1 = initializeShift(VALID_SHIFT_ID, EMPLOYEE1, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);
        Shift shift2 = initializeShift(VALID_SHIFT_ID+1, EMPLOYEE2, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(shift1);
            shifts.add(shift2);
            return shifts;
        });
        List<Shift> allShifts = service.getAllShiftsForEmployee(EMPLOYEE1);
        assertEquals(1, allShifts.size());
        assertEquals(shift1, allShifts.get(0));
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
        Shift shift1 = initializeShift(VALID_SHIFT_ID, EMPLOYEE1, SPECIFIC_WEEK_DAY1, START_TIME, END_TIME);
        Shift shift2 = initializeShift(VALID_SHIFT_ID+1, EMPLOYEE2, SPECIFIC_WEEK_DAY2, START_TIME, END_TIME);
        lenient().when(shiftRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Shift> shifts = new ArrayList<Shift>();
            shifts.add(shift1);
            shifts.add(shift2);
            return shifts;
        });
        List<Shift> allShifts = service.getAllShiftsForDay(MONDAY);
        assertEquals(1, allShifts.size());
        assertEquals(shift1, allShifts.get(0));
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

    private Shift initializeShift(int shiftId, Employee employee, SpecificWeekDay specificWeekDay, Time startTime, Time endTime){
        Shift shift = new Shift();
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
        shift.setDayOfTheWeek(specificWeekDay);
        shift.setEmployee(employee);
        System.out.println("initializing shiftId to " + shiftId);
        boolean isSet = shift.setShiftId(shiftId);
        System.out.println(isSet);
        System.out.println(shift.getShiftId());

        return shift;
    }

    private Employee initializeEmployee(String username, MuseumManagementSystem mms){
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setMuseumManagementSystem(mms);

        return employee;
    }
}
