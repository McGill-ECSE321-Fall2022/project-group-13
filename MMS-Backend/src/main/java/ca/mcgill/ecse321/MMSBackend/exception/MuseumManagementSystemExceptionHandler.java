package ca.mcgill.ecse321.MMSBackend.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MuseumManagementSystemExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MuseumManagementSystemException.class)
    public ResponseEntity<String> handleEventRegistrationException(MuseumManagementSystemException ex) {
        return new ResponseEntity<String>(ex.getMessage(), ex.getStatus());
    }

}
