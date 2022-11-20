package ca.mcgill.ecse321.MMSBackend.dto;

import java.sql.Time;
/**
 * DTO Shift class 
 * @author Samantha Perez Hoffman (samperezh)
 */
public class ShiftDto {

    private int shiftId; 
    private Time startTime;
    private Time endTime;
    private SpecificWeekDayDto dayOfTheWeek;
    private MuseumManagementSystemDto museumManagementSystem;
    private EmployeeDto employee;

    public ShiftDto() {

    }

    public ShiftDto(int shiftId, Time startTime, Time endTime, SpecificWeekDayDto specificWeekDay, MuseumManagementSystemDto museumManagementSystem, EmployeeDto employee) {
        this.shiftId = shiftId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfTheWeek = specificWeekDay;
        this.museumManagementSystem = museumManagementSystem;
        this.employee = employee;
    }

    public int getShiftId() {
        return shiftId;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public SpecificWeekDayDto getSpecificWeekDay() {
        return dayOfTheWeek;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

}
