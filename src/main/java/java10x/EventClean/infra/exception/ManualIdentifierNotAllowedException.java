package java10x.EventClean.infra.exception;

public class ManualIdentifierNotAllowedException extends RuntimeException{

    public ManualIdentifierNotAllowedException(String message) {
        super(message);
    }

}
