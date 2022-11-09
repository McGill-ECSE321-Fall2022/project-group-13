package ca.mcgill.ecse321.MMSBackend.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class MuseumManagementSystemException extends RuntimeException {

    private HttpStatus status;

    public MuseumManagementSystemException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}