package java10x.EventClean.infra.exception;

public class EventNotExistsException extends RuntimeException{

    public EventNotExistsException(String message) {
        super(message);
    }
}
