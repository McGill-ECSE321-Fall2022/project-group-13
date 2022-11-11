package ca.mcgill.ecse321.MMSBackend.dto;


/**
 * TicketDto class
 * @author Lucy Zhang (Lucy-Zh)
 */
public class TicketDto {
    
    //Ticket attributes
    private int ticketId;
    private double fee;
    private boolean isActive;
    private ClientDto client;
    private MuseumManagementSystemDto museumManagementSystem;


    public TicketDto() {

    }

    public TicketDto(int ticketId, double fee, boolean isActive, MuseumManagementSystemDto museumManagementSystem, ClientDto client) {

        this.ticketId = ticketId;
        this.fee = fee;
        this.isActive = isActive;
        this.museumManagementSystem = museumManagementSystem;
        this.client = client;
    }

    public int getTicketId() {
        return ticketId;
    }

    public double getFee() {
        return fee;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }

    public ClientDto getClient() {
        return client;
    }
}
