package App.Exception;

public class IllegalActionException extends RuntimeException {

    public IllegalActionException() {
        super("Ação não permitida");
    }

    public IllegalActionException(String message) {
        super(message);
    }

}
