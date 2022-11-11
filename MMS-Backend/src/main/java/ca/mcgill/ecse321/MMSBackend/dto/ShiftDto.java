package ca.mcgill.ecse321.MMSBackend.dto;

import java.sql.Time;
/**
 * DTO Shift class 
 * @Author: Samantha Perez Hoffman (samperezh)
 */
public class ShiftDto {

   // private int shiftId; 
    private Time startTime;
    private Time endTime;
    private SpecificWeekDayDto specificWeekDay;
    private MuseumManagementSystemDto museumManagementSystem;
    private EmployeeDto employee;

    public ShiftDto() {

    }

    public ShiftDto(Time startTime, Time endTime, SpecificWeekDayDto specificWeekDay, MuseumManagementSystemDto museumManagementSystem, EmployeeDto employee) {
        //this.shiftId = shiftId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.specificWeekDay = specificWeekDay;
        this.museumManagementSystem = museumManagementSystem;
        this.employee = employee;
    }

    // public int getShiftId() {
    //     return shiftId;
    // }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    } 

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public SpecificWeekDayDto getSpecificWeekDay() {
        return specificWeekDay;
    }

    public void setSpecificWeekDay(SpecificWeekDayDto specificWeekDay) {
        this.specificWeekDay = specificWeekDay;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

}
