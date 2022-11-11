package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * DTO SpecificWeekDay class
 * @Author: Samantha Perez Hoffman (samperezh)
 */
public class SpecificWeekDayDto {
    
    public enum DayTypeDto { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday };
    
    private DayTypeDto dayType;
    private boolean isClosed;
    private MuseumManagementSystemDto museumManagementSystem;

    public SpecificWeekDayDto(){

    }

    public SpecificWeekDayDto(DayTypeDto dayType, boolean isClosed, MuseumManagementSystemDto museumManagementSystem){
        this.dayType = dayType;
        this.isClosed = isClosed;
        this.museumManagementSystem = museumManagementSystem;
    }

    public DayTypeDto getDayType() {
        return dayType;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }

}
