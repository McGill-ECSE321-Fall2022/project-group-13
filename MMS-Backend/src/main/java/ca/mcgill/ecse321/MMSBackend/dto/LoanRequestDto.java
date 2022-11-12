package ca.mcgill.ecse321.MMSBackend.dto;

import ca.mcgill.ecse321.MMSBackend.model.LoanRequest;

/**
 * DTO LoanRequest class
 * @Author: Nazia Chowdhury (naziaC)
 */
public class LoanRequestDto {

    private int requestId;
    private int loanDuration;
    private double fee;
    private LoanRequest.LoanStatus status;
    private MuseumManagementSystemDto museumManagementSystem;

    public LoanRequestDto() {

    }

    public LoanRequestDto(int requestId, int loanDuration, double fee, LoanRequest.LoanStatus status, MuseumManagementSystemDto museumManagementSystem) {

        this.requestId = requestId;
        this.loanDuration = loanDuration;
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

    public LoanRequest.LoanStatus getStatus() {
        return status;
    }

    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return museumManagementSystem;
    }
}
