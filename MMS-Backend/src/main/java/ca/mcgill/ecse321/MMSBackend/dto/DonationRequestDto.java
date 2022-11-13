package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * @author Yu An Lu (yu-an-lu)
 * 
 * DTO DonationRequest class
 * 
 */
public class DonationRequestDto {

    public enum DonationStatusDto { Approved, Rejected, Pending };

    private int requestId;
    private ClientDto client;
    private ArtifactDto artifact;
    private DonationStatusDto status;
    private MuseumManagementSystemDto museumManagementSystem;

    public DonationRequestDto() {

    }

    public DonationRequestDto(int requestId, ClientDto client, ArtifactDto artifact, DonationStatusDto status, MuseumManagementSystemDto museumManagementSystem) {
        this.requestId = requestId;
        this.client = client;
        this.artifact = artifact;
        this.status = status;
        this.museumManagementSystem = museumManagementSystem;
    }

    public int getRequestId() {
        return requestId;
    }

    public ClientDto getClient() {
        return client;
    }

    public ArtifactDto getArtifact() {
        return artifact;
    }

    public DonationStatusDto getStatus() {
        return status;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }
}
