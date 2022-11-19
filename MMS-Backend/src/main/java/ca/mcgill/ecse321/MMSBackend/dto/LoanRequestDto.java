package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * DTO LoanRequest class
 *
 * @Author: Nazia Chowdhury (naziaC)
 */
public class LoanRequestDto {

    public enum LoanRequestStatusDto {Approved, Rejected, Pending, Returned}

    private int requestId;
    private int loanDuration;
    private double fee;
    private ClientDto client;
    private ArtifactDto artifact;
    private LoanRequestStatusDto status;
    private MuseumManagementSystemDto museumManagementSystem;


    public LoanRequestDto() {
    }

    public LoanRequestDto(int requestId, int loanDuration, double fee, ClientDto client, ArtifactDto artifact, LoanRequestStatusDto status, MuseumManagementSystemDto museumManagementSystem) {
        this.requestId = requestId;
        this.loanDuration = loanDuration;
        this.client = client;
        this.artifact = artifact;
        this.fee = fee;
        this.status = status;
        this.museumManagementSystem = museumManagementSystem;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getLoanDuration() {
        return loanDuration;
    }

    public double getFee() {
        return fee;
    }

    public LoanRequestStatusDto getStatus() {
        return status;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }

    public ClientDto getClient() {
        return client;
    }

    public ArtifactDto getArtifact() {
        return artifact;
    }
}
