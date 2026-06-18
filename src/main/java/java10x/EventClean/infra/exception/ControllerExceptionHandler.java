package java10x.EventClean.infra.exception;

import java10x.EventClean.core.entities.Evento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateEventException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEventException(DuplicateEventException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("Error: ", exc.getMessage());
        response.put("Message: ", "Please, insert a valid hashId for your event and try again!");

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EventNotExistsException.class)
    public ResponseEntity<Map<String, String>> handleEventNotExistsException(EventNotExistsException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("Error: ", exc.getMessage());
        response.put("Message: ", "Please, insert a valid hashId for your event and try again!");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
