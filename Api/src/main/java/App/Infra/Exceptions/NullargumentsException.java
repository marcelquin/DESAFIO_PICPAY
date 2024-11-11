package App.Infra.Exceptions;

public class NullargumentsException extends RuntimeException {

    public NullargumentsException() {
        super("Todos os campos devem ser preenchidos");
    }

    public NullargumentsException(String message) {
        super(message);
    }

}
