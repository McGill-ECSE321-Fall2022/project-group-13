package ca.mcgill.ecse321.MMSBackend.dto;
public class ArtifactDto {

    public enum LoanStatusDto { Available, Unavailable, Loaned }
    private int artifactId;
    private String name;
    private String image;
    private String description;
    private LoanStatusDto loanStatus;
    private boolean isDamaged;
    private double loanFee;
    private double worth;
    private RoomDto roomLocation;
    private MuseumManagementSystemDto museumManagementSystem;

    public ArtifactDto(){

    }

    public ArtifactDto(int artifactId, String name, String image, String description, LoanStatusDto loanStatus,
                       boolean isDamaged, double loanFee, double worth, RoomDto roomLocation, MuseumManagementSystemDto
                               museumManagementSystem){
        this.artifactId = artifactId;
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

    public int getArtifactId() {return artifactId;}

    public String getName(){
        return name;
    }

    public String getImage(){
        return image;
    }

    public String getDescription(){
        return description;
    }

    public LoanStatusDto getLoanStatus(){
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
