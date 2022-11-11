package ca.mcgill.ecse321.MMSBackend.dto;

import ca.mcgill.ecse321.MMSBackend.model.Artifact;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.model.Room;

public class ArtifactDto {

    public enum LoanStatus { Available, Unavailable, Loaned }
   // private int artifactId;
    private String name;
    private String image;
    private String description;
    private LoanStatus loanStatus;
    private boolean isDamaged;
    private double loanFee;
    private double worth;
    private RoomDto roomLocation;
    private MuseumManagementSystemDto museumManagementSystem;

    public ArtifactDto(){

    }

    public ArtifactDto(String name, String image, String description, LoanStatus loanStatus, boolean isDamaged, double
            loanFee, double worth, RoomDto roomLocation, MuseumManagementSystemDto museumManagementSystem){
        this.name = name;
        this.image = image;
        this.description = description;
        this.loanStatus = loanStatus;
        this.isDamaged = isDamaged;
        this.loanFee = loanFee;
        this.worth = worth;
        this.roomLocation = roomLocation;
        this.museumManagementSystem = museumManagementSystem;
    }

    public String getName(){
        return name;
    }

    public String getImage(){
        return image;
    }

    public String getDescription(){
        return description;
    }

    public LoanStatus getLoanStatus(){
        return loanStatus;
    }

    public boolean getIsDamaged(){
        return isDamaged;
    }

    public double getLoanFee(){
        return loanFee;
    }

    public double getWorth(){
        return worth;
    }

    public RoomDto getRoomLocation() {
        return roomLocation;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }
}
