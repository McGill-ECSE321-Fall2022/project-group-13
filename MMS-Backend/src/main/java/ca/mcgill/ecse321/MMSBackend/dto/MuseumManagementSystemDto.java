package ca.mcgill.ecse321.MMSBackend.dto;

import java.sql.Time;

/**
 * MuseumManagementSystemDto class
 * @author Lucy Zhang (Lucy-Zh)
 */
public class MuseumManagementSystemDto {

    private int museumManagementSystemId;
    private String name;
    private Time openingTime;
    private Time closingTime;
    private int maxLoanNumber;
    private double ticketFee;

    public MuseumManagementSystemDto() {

    }

    public MuseumManagementSystemDto(int museumManagementSystemId, String name, Time openingTime, Time closingTime, int maxLoanNumber, double ticketFee) {
        this.museumManagementSystemId = museumManagementSystemId;
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.maxLoanNumber = maxLoanNumber;
        this.ticketFee = ticketFee;
    }

    public int getMuseumManagementSystemId() {
        return museumManagementSystemId;
    }

    public String getName() {
        return name;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public int getMaxLoanNumber() {
        return maxLoanNumber;
    }

    public double getTicketFee() {
        return ticketFee;
    }
}
