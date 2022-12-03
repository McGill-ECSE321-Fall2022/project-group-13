package ca.mcgill.ecse321.MMSBackend.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.MMSBackend.dto.EmployeeDto;
import ca.mcgill.ecse321.MMSBackend.dto.MuseumManagementSystemDto;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MMSBackend.dto.ShiftDto;
import ca.mcgill.ecse321.MMSBackend.dto.SpecificWeekDayDto;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Shift;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;
import ca.mcgill.ecse321.MMSBackend.service.EmployeeAccountService;
import ca.mcgill.ecse321.MMSBackend.service.EmployeeScheduleService;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Samantha Perez Hoffman (samperezh)
 * The EmployeeScheduleRestController class is responsible for exposing
 * the business logic declared in EmployeeScheduleService using a REST
 * API.
 */
@CrossOrigin(origins = "*")
@RestController
public class EmployeeScheduleRestController {

    @Autowired
    private EmployeeScheduleService employeeScheduleService;

    @Autowired
    private EmployeeAccountService employeeAccountService;

    @Autowired
    private MuseumManagementSystemService mmsService;

    /**
     * Create a new shift 
     * 
     * @author Samantha Perez Hoffman (samperezh)
     * @param employeeUsername
     * @param day
     * @param startTime
     * @param endTime
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/createShift", "/createShift/" })
    public ResponseEntity<ShiftDto> createShift(@RequestParam String employeeUsername,
            @RequestParam String day, @RequestParam Time startTime,
            @RequestParam Time endTime) throws IllegalArgumentException {
        Employee employee = employeeAccountService.getEmployee(employeeUsername);
        DayType dayType = convertStringToDayType(day);
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        Shift shift = employeeScheduleService.createShift(employee, specificWeekDay, startTime, endTime);
        return new ResponseEntity<>(convertToDto(shift), HttpStatus.CREATED);
    }


    /**
     * Delete a shift from its shiftId
     * 
     * @author Samantha Perez Hoffman (samperezh)
     * @param shiftId
     */
    @DeleteMapping(value = { "/deleteShift/{shiftId}", "/deleteShift/{shiftId}/" })
    public void deleteShift(@PathVariable("shiftId") int shiftId) {
        employeeScheduleService.deleteShift(shiftId);
    }

    /**
     * Delete all shifts for a specific day 
     * 
     * @author Samantha Perez Hoffman (samperezh)
     * @param dayType
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/shift/day", "/shift/day/" })
    public void deleteAllShiftsForDay(@RequestParam String dayType)
            throws IllegalArgumentException {
        employeeScheduleService.deleteAllShiftsForDay(convertStringToDayType(dayType));
    }

    /**
     * Delete all shifts for an employee
     * 
     * @author Samantha Perez Hoffman (samperezh)
     * @param employeeUsername
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/shift/employee", "/shift/employee/" })
    public void deleteAllShiftsForEmployee(@RequestParam String employeeUsername)
            throws IllegalArgumentException {
        Employee employee = employeeAccountService.getEmployee(employeeUsername);
        employeeScheduleService.deleteAllShiftsForEmployee(employee);
    }

    /**
     * Get all shifts 
     * 
     * @author Samantha Perez Hoffman (samperezh)
     */
    @GetMapping(value = { "/shift", "/shift/" })
    public ResponseEntity<List<ShiftDto>> getAllShifts() {
        List<ShiftDto> shiftsDto = new ArrayList<>();
        for (Shift shift : employeeScheduleService.getAllShifts()) {
            shiftsDto.add(convertToDto(shift));
        }
        return new ResponseEntity<>(shiftsDto , HttpStatus.OK);
    }

    /**
     * Get all shifts for an employee
     *
     * @author Samantha Perez Hoffman (samperezh)
     * @param employeeUsername
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/shift/employee", "/shift/employee/" })
    public ResponseEntity<List<ShiftDto>> getAllShiftsForEmployee(@RequestParam String employeeUsername)
            throws IllegalArgumentException {
        Employee employee = employeeAccountService.getEmployee(employeeUsername);
        List<ShiftDto> shiftsDto = new ArrayList<>();
        for (Shift shift : employeeScheduleService.getAllShiftsForEmployee(employee)) {
            shiftsDto.add(convertToDto(shift));
        }
        return new ResponseEntity<>(shiftsDto , HttpStatus.OK);
    }

    /**
     * Get all shifts for a specific day 
     *
     * @author Samantha Perez Hoffman (samperezh)
     * @param day
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/shift/day", "/shift/day/" })
    public ResponseEntity<List<ShiftDto>> getAllShiftsForDay(@RequestParam String day)
            throws IllegalArgumentException {
        DayType dayType = convertStringToDayType(day);
        List<ShiftDto> shiftsDto = new ArrayList<>();
        for (Shift shift : employeeScheduleService.getAllShiftsForDay(dayType)) {
            shiftsDto.add(convertToDto(shift));
        }
        return new ResponseEntity<>(shiftsDto , HttpStatus.OK);
    }

    /**
     * Update a shift's start and end time as well as its day
     * 
     * @author Samantha Perez Hoffman (samperezh)
     * @param shiftId
     * @param startTime
     * @param endTime
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/shift/update/{shiftId}", "/shift/update/{shiftId}/" })
    public ResponseEntity<ShiftDto> updateShift(@PathVariable("shiftId") int shiftId, @RequestParam Time startTime,
            @RequestParam Time endTime, @RequestParam String day) throws IllegalArgumentException {
        DayType dayType = convertStringToDayType(day);
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        Shift shift = employeeScheduleService.updateShift(shiftId, startTime, endTime, specificWeekDay);
        return new ResponseEntity<>(convertToDto(shift), HttpStatus.OK);
    }

    /**
     * Update a specificWeekDay isClosed status
     * 
     * @author Samantha Perez Hoffman (samperezh)
     * @param day
     * @param isClosed
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/specificWeekDay", "/specificWeekDay/" })
    public ResponseEntity<SpecificWeekDayDto> updateSpecificWeekDayStatus(
            @RequestParam String day, @RequestParam boolean isClosed)
            throws IllegalArgumentException {
        DayType dayType = convertStringToDayType(day);
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        specificWeekDay = employeeScheduleService.updateSpecificWeekDayStatus(specificWeekDay, isClosed);

        return new ResponseEntity<>(convertToDto(specificWeekDay), HttpStatus.OK);
    }

    // Helper methods to convert classes from the model into DTOs and vice-versa

    private DayType convertStringToDayType(String day){
        if (day == null) {
            throw new IllegalArgumentException("DayType cannot be null!");
        }
        return switch (day) {
            case "Monday" -> DayType.Monday;
            case "Tuesday" -> DayType.Tuesday;
            case "Wednesday" -> DayType.Wednesday;
            case "Thursday" -> DayType.Thursday;
            case "Friday" -> DayType.Friday;
            case "Saturday" -> DayType.Saturday;
            case "Sunday" -> DayType.Sunday;
            default -> throw new IllegalArgumentException("Unexpected value: " + day);
        };
    }

    private ShiftDto convertToDto(Shift shift) {
        if (shift == null) {
            throw new IllegalArgumentException("Shift cannot be null!");
        }
        return new ShiftDto(shift.getShiftId(), shift.getStartTime(), shift.getEndTime(),
                convertToDto(shift.getDayOfTheWeek()), convertToDto(shift.getMuseumManagementSystem()),
                convertToDto(shift.getEmployee()));
    }

    private SpecificWeekDayDto convertToDto(SpecificWeekDay specificWeekDay) {
        if (specificWeekDay == null) {
            throw new IllegalArgumentException("SpecificWeekDay cannot be null!");
        }
        return new SpecificWeekDayDto(convertToDto(specificWeekDay.getDayType()), specificWeekDay.getIsClosed(),
                convertToDto(specificWeekDay.getMuseumManagementSystem()));
    }

    private SpecificWeekDayDto.DayTypeDto convertToDto(DayType dayType) {
        if (dayType == null) {
            throw new IllegalArgumentException("DayType cannot be null!");
        }
        return switch (dayType) {
            case Monday -> SpecificWeekDayDto.DayTypeDto.Monday;
            case Tuesday -> SpecificWeekDayDto.DayTypeDto.Tuesday;
            case Wednesday -> SpecificWeekDayDto.DayTypeDto.Wednesday;
            case Thursday -> SpecificWeekDayDto.DayTypeDto.Thursday;
            case Friday -> SpecificWeekDayDto.DayTypeDto.Friday;
            case Saturday -> SpecificWeekDayDto.DayTypeDto.Saturday;
            case Sunday -> SpecificWeekDayDto.DayTypeDto.Sunday;
            default -> throw new IllegalArgumentException("Unexpected value: " + dayType);
        };
    }

    private MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms) {
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        return new MuseumManagementSystemDto(mms.getSystemId(), mms.getName(), mms.getOpenTime(), mms.getCloseTime(), mms.getMaxLoanNumber(), mms.getTicketFee());
    }

    private EmployeeDto convertToDto(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException("There is no such Employee!");
        }
        MuseumManagementSystemDto mmsDto = convertToDto(employee.getMuseumManagementSystem());

        return new EmployeeDto(employee.getUsername(), employee.getName(),
                employee.getPassword(), mmsDto);
    }
}
