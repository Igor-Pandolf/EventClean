package java10x.EventClean.infra.exception;

public class DuplicateEventException extends RuntimeException{

    public DuplicateEventException(String message) {
        super(message);
    }
}
