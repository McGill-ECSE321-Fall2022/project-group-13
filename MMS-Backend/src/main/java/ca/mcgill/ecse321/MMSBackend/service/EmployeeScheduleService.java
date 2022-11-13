package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.MMSBackend.dao.ShiftRepository;
import ca.mcgill.ecse321.MMSBackend.dao.SpecificWeekDayRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Shift;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;

/**
 * @author Samantha Perez Hoffman (samaperezh)
 *         The EmployeeScheduleService class implements the use case based on
 *         the requirement:
 *         “FREQ04: The museum management system shall only allow the manager to
 *         update the opening hours, the list of working employees, their start
 *         and end times on a particular day as well as mark a certain day as
 *         closed.”
 */
@Service
public class EmployeeScheduleService {

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    SpecificWeekDayRepository specificWeekDayRepository;

    /**
     * Create new shift
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @parm employee
     * @parm specifcWeekDay
     * @parm startTime
     * @parm endTime
     */
    @Transactional
    public Shift createShift(Employee employee, SpecificWeekDay specificWeekDay, Time startTime, Time endTime) {
        // check input values are not null
        if (employee == null || specificWeekDay == null || startTime == null || endTime == null) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values are not allowed!");
        }

        // check if startTime is before endTime
        if (startTime.after(endTime)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Start time must be before end time!");
        }

        // check if specificWeekDay is marked as closed
        if (specificWeekDay.getIsClosed()) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST,
                    "This day is closed, cannot create shift");
        }

        // check if employee already has shift that overlaps with this new shift
        List<Shift> employeeShifts = getAllShiftsForEmployee(employee);
        for (Shift currentShift : employeeShifts) {
            if (currentShift.getDayOfTheWeek().equals(specificWeekDay)) {

                if ((currentShift.getStartTime().before(endTime) && currentShift.getEndTime().after(startTime))
                        || currentShift.getStartTime().equals(startTime) || currentShift.getEndTime().equals(endTime)) {
                    throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST,
                            "Employee already has a shift that overlaps with this new shift");
                }
            }
        }

        Shift shift = new Shift();
        shift.setEmployee(employee);
        shift.setDayOfTheWeek(specificWeekDay);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
        shift.setMuseumManagementSystem(employee.getMuseumManagementSystem());
        shiftRepository.save(shift);
        return shift;
    }

    /**
     * Delete shift from its shiftId
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @parm shiftId
     */
    @Transactional
    public void deleteShift(int ShiftId) {
        shiftRepository.deleteById(ShiftId);
    }

    /**
     * Delete all shifts for a specific day
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @parm dayType
     */
    @Transactional
    public void deleteAllShiftsForDay(DayType dayType) {
        if (dayType == null) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values are not allowed!");
        }
        List<Shift> shifts = getAllShiftsForDay(dayType);
        for (Shift shift : shifts) {
            deleteShift(shift.getShiftId());
        }
    }

    /**
     * Delete all shifts for a specific employee
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param employee
     */
    @Transactional
    public void deleteAllShiftsForEmployee(Employee employee) {
        List<Shift> shifts = getAllShiftsForEmployee(employee);
        for (Shift shift : shifts) {
            deleteShift(shift.getShiftId());
        }
    }

    /**
     * Get all shifts
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     */
    @Transactional
    public List<Shift> getAllShifts() {
        return toList(shiftRepository.findAll());
    }

    /**
     * Get all shifts for a specific employee
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param employee
     */
    @Transactional
    public List<Shift> getAllShiftsForEmployee(Employee employee) {
        if (employee == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Employee does not exist!");
        }

        List<Shift> allShifts = getAllShifts();
        List<Shift> employeeShifts = new ArrayList<Shift>();
        for (Shift shift : allShifts) {
            if (shift.getEmployee() == employee) {
                employeeShifts.add(shift);
            }
        }

        //return allShifts;
        return employeeShifts;
    }

    /**
     * Get all shifts for a specific day
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param dayType
     */
    @Transactional
    public List<Shift> getAllShiftsForDay(DayType dayType) {
        if (dayType == null) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values are not allowed!");
        }
        List<Shift> allShifts = getAllShifts();
        List<Shift> dayShifts = new ArrayList<Shift>();
        for (Shift shift : allShifts) {
            if (shift.getDayOfTheWeek().getDayType() == dayType) {
                dayShifts.add(shift);
            }
        }

        return dayShifts;
    }

    /**
     * edit shift start and end time
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param shiftId
     * @param startTime
     * @param endTime
     */
    @Transactional
    public Shift updateShiftStartEndTime(int shiftId, Time newStartTime, Time newEndTime) {
        Shift shift = shiftRepository.findShiftByShiftId(shiftId);
        if (shift == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "No shift with id " + shiftId + " exists.");
        }

        if (newStartTime == null || newEndTime == null) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values are not allowed!");
        }

        // checks if newStartTime is after newEndTime
        if (newStartTime.after(newEndTime)) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Start time cannot be after end time.");
        }

        // check if employee already has shift that overlaps with updated shift
        if (doesNewShiftOverlap(shift, shift.getDayOfTheWeek().getDayType())) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST,
                    "Employee already has a shift that overlaps with this new shift");
        }

        shift.setStartTime(newStartTime);
        shift.setEndTime(newEndTime);
        shiftRepository.save(shift);
        return shift;
    }

    /**
     * edit shift's specific week day
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param shiftId
     * @param specificWeekDay
     */
    @Transactional
    public Shift updateShiftDay(int shiftId, SpecificWeekDay specificWeekDay) {
        Shift shift = shiftRepository.findShiftByShiftId(shiftId);
        if (shift == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "No shift with id " + shiftId + " exists.");
        }
        if (specificWeekDay == null) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Null values are not allowed!");
        }

        // check if newDayType is marked as closed
        if (specificWeekDay.getIsClosed()) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST,
                    "This day is closed, cannot update shift");
        }

        // check if employee already has shift that overlaps with updated shift
        if (doesNewShiftOverlap(shift, specificWeekDay.getDayType())) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST,
                    "Employee already has a shift that overlaps with this new shift");
        }

        shift.setDayOfTheWeek(specificWeekDay);
        shiftRepository.save(shift);
        return shift;
    }

    /**
     * update SpecificWeekDay isClosed attribute
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param specificWeekDay
     * @param isClosed
     */
    @Transactional
    public SpecificWeekDay updateSpecificWeekDayStatus(SpecificWeekDay specificWeekDay, boolean isClosed) {
        if (specificWeekDay == null) {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "No specificWeekDay exists.");
        }

        List<Shift> allShifts = getAllShiftsForDay(specificWeekDay.getDayType());

        // checks if the user is trying to set a specificWeekDay as closed but there are
        // shifts scheduled for that day.
        if ((!allShifts.isEmpty()) && isClosed) {
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST,
                    "Cannot close day with that has scheduled shifts.");
        }

        specificWeekDay.setIsClosed(isClosed);
        specificWeekDayRepository.save(specificWeekDay);
        return specificWeekDay;
    }

    /**
     * toList helper method (@author eventRegistration authors)
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

    /**
     * helper method that checks if a shift overlaps with any other existing shifts
     * 
     * @Author: Samantha Perez Hoffman (samperezh)
     * @param newShift
     * @param dayType
     */
    private boolean doesNewShiftOverlap(Shift newShift, DayType dayType) {
        Employee employee = newShift.getEmployee();
        Time newStartTime = newShift.getStartTime();
        Time newEndTime = newShift.getEndTime();
        int newShiftId = newShift.getShiftId();

        List<Shift> employeeShifts = getAllShiftsForEmployee(employee);

        for (Shift currentShift : employeeShifts) {
            if (currentShift.getDayOfTheWeek().getDayType().equals(dayType)) {
                if ((currentShift.getStartTime().before(newEndTime) && currentShift.getEndTime().after(newStartTime))
                        || currentShift.getStartTime().equals(newStartTime)
                        || currentShift.getEndTime().equals(newEndTime)) {
                    if (currentShift.getShiftId() != newShiftId) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
