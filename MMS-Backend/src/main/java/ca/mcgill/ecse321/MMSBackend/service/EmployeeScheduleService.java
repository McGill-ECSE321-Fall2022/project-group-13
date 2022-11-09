package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.MMSBackend.dao.ShiftRepository;
import ca.mcgill.ecse321.MMSBackend.dao.SpecificWeekDayRepository;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Shift;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;

@Service
public class EmployeeScheduleService {

    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    SpecificWeekDayRepository specificWeekDayRepository;

    // TODO:
    // Q: do we need to check for null input values?

    // get shift by Id?

    // create specific Week day??
    // delete specific Week day??

    // edit opening hours ???
    // get opening hours ???

    // delete all shifts for an employee -> could be useful if fire?

    /**
     * Create new shift
     */
    @Transactional
    public Shift createShift(Employee employee, SpecificWeekDay specificWeekDay, Time startTime, Time endTime) {
        // check if specificWeekDay is marked as closed
        if (specificWeekDay.getIsClosed()) {
            throw new IllegalArgumentException("This day is closed, cannot create shift");
        }

        // check if employee already has shift that overlaps with this new shift
        List<Shift> employeeShifts = getAllShiftsForEmployee(employee);
        for (Shift currentShift : employeeShifts) {
            if (currentShift.getDayOfTheWeek().equals(specificWeekDay)) {

                if ((currentShift.getStartTime().before(endTime) && currentShift.getEndTime().after(startTime))
                        || currentShift.getStartTime().equals(startTime) || currentShift.getEndTime().equals(endTime)) {
                    throw new IllegalArgumentException(
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
     * Delete shift
     * Note that "If the entity is not found in the persistence store it is silently
     * ignored."
     */
    @Transactional
    public void deleteShift(int ShiftId) {
        shiftRepository.deleteById(ShiftId);
    }

    /**
     * Delete all shifts for a specific day
     */
    @Transactional
    public void deleteAllShiftsForDay(DayType dayType) {
        List<Shift> shifts = getAllShiftsForDay(dayType);
        for (Shift shift : shifts) {
            deleteShift(shift.getShiftId());
        }
    }

    /**
     * Get all shifts
     */
    @Transactional
    public List<Shift> getAllShifts() {
        return toList(shiftRepository.findAll());
    }

    /**
     * Get all shifts for a specific employee
     */
    @Transactional
    public List<Shift> getAllShiftsForEmployee(Employee employee) {
        List<Shift> allShifts = getAllShifts();
        for (Shift shift : allShifts) {
            if (shift.getEmployee() != employee) {
                allShifts.remove(shift);
            }
        }

        return allShifts;
    }

    /**
     * Get all shifts for a specific day
     */
    @Transactional
    public List<Shift> getAllShiftsForDay(DayType dayType) {
        List<Shift> allShifts = getAllShifts();
        for (Shift shift : allShifts) {
            if (shift.getDayOfTheWeek().getDayType() != dayType) {
                allShifts.remove(shift);
            }
        }

        return allShifts;
    }

    /**
     * edit shift start and end time
     */
    @Transactional
    public Shift updateShiftStartEndTime(int shiftId, Time newStartTime, Time newEndTime) {
        Shift shift = shiftRepository.findShiftByShiftId(shiftId);
        if (shift == null) {
            throw new IllegalArgumentException("No shift with id " + shiftId + " exists.");
        }

        if (newStartTime.after(newEndTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time.");
        }

        // check if employee already has shift that overlaps with updated shift
        if (doesNewShiftOverlap(shift, shift.getDayOfTheWeek().getDayType())) {
            throw new IllegalArgumentException(
                    "Employee already has a shift that overlaps with this new shift");
        }

        shift.setStartTime(newStartTime);
        shift.setEndTime(newEndTime);
        shiftRepository.save(shift);
        return shift;
    }

    /**
     * edit shift's specific week day 
     */
    @Transactional
    public Shift updateShiftDay(int shiftId, DayType newDayType) {
        Shift shift = shiftRepository.findShiftByShiftId(shiftId);
        if (shift == null) {
            throw new IllegalArgumentException("No shift with id " + shiftId + " exists.");
        }

        // check if newDayType is marked as closed
        SpecificWeekDay specificWeekDay = specificWeekDayRepository.findSpecificWeekDayByDayType(newDayType);
        if (specificWeekDay.getIsClosed()) {
            throw new IllegalArgumentException("This day is closed, cannot update shift");
        }

        // check if employee already has shift that overlaps with updated shift
        if (doesNewShiftOverlap(shift, newDayType)) {
            throw new IllegalArgumentException(
                    "Employee already has a shift that overlaps with this new shift");
        }

        shift.setDayOfTheWeek(specificWeekDay);
        shiftRepository.save(shift);
        return shift;
    }

    /**
     * update SpecificWeekDay isClosed attribute
     */
    @Transactional
    public SpecificWeekDay updateSpecificWeekDayStatus(DayType dayType, boolean isClosed) {
        SpecificWeekDay day = specificWeekDayRepository.findSpecificWeekDayByDayType(dayType);
        if (day == null) {
            throw new IllegalArgumentException("No day with type " + dayType + " exists.");
        }

        List<Shift> allShifts = getAllShiftsForDay(dayType);

        if (allShifts.isEmpty() && isClosed) {
            throw new IllegalArgumentException("Cannot close day with that has scheduled shifts.");
        }

        day.setIsClosed(isClosed);
        specificWeekDayRepository.save(day);
        return day;
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
     */
    private boolean doesNewShiftOverlap(Shift newShift, DayType dayType) {
        // check if employee already has shift that overlaps with updated shift
        Employee employee = newShift.getEmployee();
        Time startTime = newShift.getStartTime();
        Time endTime = newShift.getEndTime();
        int shiftId = newShift.getShiftId();

        List<Shift> employeeShifts = getAllShiftsForEmployee(employee);

        for (Shift currentShift : employeeShifts) {
            if (currentShift.getDayOfTheWeek().getDayType().equals(dayType)) {
                if ((currentShift.getStartTime().before(endTime) && currentShift.getEndTime().after(startTime))
                        || currentShift.getStartTime().equals(startTime) || currentShift.getEndTime().equals(endTime)) {
                    if (currentShift.getShiftId() != shiftId) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
