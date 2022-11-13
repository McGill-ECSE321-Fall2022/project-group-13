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

import ca.mcgill.ecse321.MMSBackend.dto.EmployeeDto;
import ca.mcgill.ecse321.MMSBackend.dto.ShiftDto;
import ca.mcgill.ecse321.MMSBackend.dto.SpecificWeekDayDto;
import ca.mcgill.ecse321.MMSBackend.dto.SpecificWeekDayDto.DayTypeDto;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Shift;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;
import ca.mcgill.ecse321.MMSBackend.service.AccountManagementService;
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
    private AccountManagementService accountManagementService;

    @Autowired
    private MuseumManagementSystemService mmsService;

    /**
     * Create a new shift 
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param employeeDto
     * @param specificWeekDayDto
     * @param startTime
     * @param endTime
     * @return
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/shift", "/shift/" })
    public ShiftDto createShift(@RequestParam(name = "employee") EmployeeDto employeeDto,
            @RequestParam(name = "dayOfTheWeek") SpecificWeekDayDto specificWeekDayDto, @RequestParam Time startTime,
            @RequestParam Time endTime) throws IllegalArgumentException {
        Employee employee = accountManagementService.getEmployee(employeeDto.getUsername());
        DayType dayType = ToDtoHelper.convertToDomainObject(specificWeekDayDto.getDayType());
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        Shift shift = employeeScheduleService.createShift(employee, specificWeekDay, startTime, endTime);

        return ToDtoHelper.convertToDto(shift);
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
     * @param dayTypeDto
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/shift/day", "/shift/day/" })
    public void deleteAllShiftsForDay(@RequestParam(name = "dayType") DayTypeDto dayTypeDto)
            throws IllegalArgumentException {
        employeeScheduleService.deleteAllShiftsForDay(ToDtoHelper.convertToDomainObject(dayTypeDto));
    }

    /**
     * Delete all shifts for an employee
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param employeeDto
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/shift/employee", "/shift/employee/" })
    public void deleteAllShiftsForEmployee(@RequestParam(name = "employee") EmployeeDto employeeDto)
            throws IllegalArgumentException {
        Employee employee = accountManagementService.getEmployee(employeeDto.getUsername());
        employeeScheduleService.deleteAllShiftsForEmployee(employee);
    }

    /**
     * Get all shifts 
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @return
     */
    @GetMapping(value = { "/shift", "/shift/" })
    public List<ShiftDto> getAllShifts() {
        List<ShiftDto> shiftsDto = new ArrayList<ShiftDto>();
        for (Shift shift : employeeScheduleService.getAllShifts()) {
            shiftsDto.add(ToDtoHelper.convertToDto(shift));
        }
        return shiftsDto;
    }

    /**
     * Get all shifts for an employee
     *
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param employeeDto
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/shift/employee", "/shift/employee/" })
    public List<ShiftDto> getAllShiftsForEmployee(@RequestParam(name = "employee") EmployeeDto employeeDto)
            throws IllegalArgumentException {
        Employee employee = accountManagementService.getEmployee(employeeDto.getUsername());
        List<ShiftDto> shiftsDto = new ArrayList<ShiftDto>();
        for (Shift shift : employeeScheduleService.getAllShiftsForEmployee(employee)) {
            shiftsDto.add(ToDtoHelper.convertToDto(shift));
        }
        return shiftsDto;
    }

    /**
     * Get all shifts for a specific day 
     *
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param dayTypeDto
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/shift/day", "/shift/day/" })
    public List<ShiftDto> getAllShiftsForDay(@RequestParam(name = "dayType") DayTypeDto dayTypeDto)
            throws IllegalArgumentException {
        DayType dayType = ToDtoHelper.convertToDomainObject(dayTypeDto);
        List<ShiftDto> shiftsDto = new ArrayList<ShiftDto>();
        for (Shift shift : employeeScheduleService.getAllShiftsForDay(dayType)) {
            shiftsDto.add(ToDtoHelper.convertToDto(shift));
        }
        return shiftsDto;
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
    public ShiftDto updateShiftStartEndTime(@PathVariable("shiftId") int shiftId, @RequestParam Time startTime,
            @RequestParam Time endTime) throws IllegalArgumentException {
        Shift shift = employeeScheduleService.updateShiftStartEndTime(shiftId, startTime, endTime);
        return ToDtoHelper.convertToDto(shift);
    }

    /**
     * Update a shift's day
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param shiftId
     * @param specificWeekDayDto
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/shift/day/{shiftId}", "/shift/day/{shiftId}/" })
    public ShiftDto updateShiftDay(@PathVariable("shiftId") int shiftId,
            @RequestParam(name = "dayOfTheWeek") SpecificWeekDayDto specificWeekDayDto)
            throws IllegalArgumentException {
        DayType dayType = ToDtoHelper.convertToDomainObject(specificWeekDayDto.getDayType());
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        Shift shift = employeeScheduleService.updateShiftDay(shiftId, specificWeekDay);
        return ToDtoHelper.convertToDto(shift);
    }

    /**
     * Update a specificWeekDay isClosed status
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param specificWeekDayDto
     * @param isClosed
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/specificWeekDay", "/specificWeekDay/" })
    public SpecificWeekDay updateSpecificWeekDayStatus(
            @RequestParam(name = "dayOfTheWeek") SpecificWeekDayDto specificWeekDayDto, @RequestParam boolean isClosed)
            throws IllegalArgumentException {
        DayType dayType = ToDtoHelper.convertToDomainObject(specificWeekDayDto.getDayType());
        SpecificWeekDay specificWeekDay = mmsService.getSpecificWeekDayByDayType(dayType);
        specificWeekDay = employeeScheduleService.updateSpecificWeekDayStatus(specificWeekDay, isClosed);
        return specificWeekDay;
    }

}
