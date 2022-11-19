package ca.mcgill.ecse321.MMSBackend.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

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
 *         The EmployeeScheduleRestController class is responsible for exposing
 *         the business logic declared in EmployeeScheduleService using a REST
 *         API.
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
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param employeeUsername
     * @param day
     * @param startTime
     * @param endTime
     * @return
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/createShift", "/createShift/" })
    public ResponseEntity<ShiftDto> createShift(@RequestParam String employeeUsername,
            @RequestParam String day, @RequestParam Time startTime,
            @RequestParam Time endTime) throws IllegalArgumentException {
        Employee employee = employeeAccountService.getEmployee(employeeUsername);
        DayType dayType = ToDtoHelper.convertStringToDayType(day);
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        Shift shift = employeeScheduleService.createShift(employee, specificWeekDay, startTime, endTime);
        return new ResponseEntity<ShiftDto>(ToDtoHelper.convertToDto(shift), HttpStatus.CREATED);
    }


    /**
     * Delete a shift from its shiftId
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param shiftId
     */
    @DeleteMapping(value = { "/shift/{shiftId}", "/shift/{shiftId}/" })
    public void deleteShift(@PathVariable("shiftId") int shiftId) {
        employeeScheduleService.deleteShift(shiftId);
    }

    /**
     * Delete all shifts for a specific day 
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param dayType
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/shift/day", "/shift/day/" })
    public void deleteAllShiftsForDay(@RequestParam String dayType)
            throws IllegalArgumentException {
        employeeScheduleService.deleteAllShiftsForDay(ToDtoHelper.convertStringToDayType(dayType));
    }

    /**
     * Delete all shifts for an employee
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
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
     * @Author: Samantha Perez Hoffman (samperezh)
     * @return
     */
    @GetMapping(value = { "/shift", "/shift/" })
    public ResponseEntity<List<ShiftDto>> getAllShifts() {
        List<ShiftDto> shiftsDto = new ArrayList<ShiftDto>();
        for (Shift shift : employeeScheduleService.getAllShifts()) {
            shiftsDto.add(ToDtoHelper.convertToDto(shift));
        }
        return new ResponseEntity<>(shiftsDto , HttpStatus.OK);
    }

    /**
     * Get all shifts for an employee
     *
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param employeeUsername
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/shift/employee", "/shift/employee/" })
    public ResponseEntity<List<ShiftDto>> getAllShiftsForEmployee(@RequestParam String employeeUsername)
            throws IllegalArgumentException {
        Employee employee = employeeAccountService.getEmployee(employeeUsername);
        List<ShiftDto> shiftsDto = new ArrayList<ShiftDto>();
        for (Shift shift : employeeScheduleService.getAllShiftsForEmployee(employee)) {
            shiftsDto.add(ToDtoHelper.convertToDto(shift));
        }
        return new ResponseEntity<>(shiftsDto , HttpStatus.OK);
    }

    /**
     * Get all shifts for a specific day 
     *
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param day
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/shift/day", "/shift/day/" })
    public ResponseEntity<List<ShiftDto>> getAllShiftsForDay(@RequestParam String day)
            throws IllegalArgumentException {
        DayType dayType = ToDtoHelper.convertStringToDayType(day);
        List<ShiftDto> shiftsDto = new ArrayList<ShiftDto>();
        for (Shift shift : employeeScheduleService.getAllShiftsForDay(dayType)) {
            shiftsDto.add(ToDtoHelper.convertToDto(shift));
        }
        return new ResponseEntity<>(shiftsDto , HttpStatus.OK);
    }

    /**
     * Update a shift's start and end time
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param shiftId
     * @param startTime
     * @param endTime
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/shift/times/{shiftId}", "/shift/times/{shiftId}/" })
    public ResponseEntity<ShiftDto> updateShiftStartEndTime(@PathVariable("shiftId") int shiftId, @RequestParam Time startTime,
            @RequestParam Time endTime) throws IllegalArgumentException {
        Shift shift = employeeScheduleService.updateShiftStartEndTime(shiftId, startTime, endTime);
        return new ResponseEntity<ShiftDto>(ToDtoHelper.convertToDto(shift), HttpStatus.OK);
    }

    /**
     * Update a shift's day
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param shiftId
     * @param day
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/shift/day/{shiftId}", "/shift/day/{shiftId}/" })
    public ResponseEntity<ShiftDto> updateShiftDay(@PathVariable("shiftId") int shiftId,
            @RequestParam String day) throws IllegalArgumentException {
        DayType dayType = ToDtoHelper.convertStringToDayType(day);
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        Shift shift = employeeScheduleService.updateShiftDay(shiftId, specificWeekDay);
        return new ResponseEntity<ShiftDto>(ToDtoHelper.convertToDto(shift), HttpStatus.OK);
    }

    /**
     * Update a specificWeekDay isClosed status
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param day
     * @param isClosed
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/specificWeekDay", "/specificWeekDay/" })
    public ResponseEntity<SpecificWeekDayDto> updateSpecificWeekDayStatus(
            @RequestParam String day, @RequestParam boolean isClosed)
            throws IllegalArgumentException {
        DayType dayType = ToDtoHelper.convertStringToDayType(day);
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        specificWeekDay = employeeScheduleService.updateSpecificWeekDayStatus(specificWeekDay, isClosed);

        return new ResponseEntity<SpecificWeekDayDto>(ToDtoHelper.convertToDto(specificWeekDay), HttpStatus.OK);
    }
}
