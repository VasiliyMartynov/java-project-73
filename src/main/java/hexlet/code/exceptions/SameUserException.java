package hexlet.code.exceptions;

public class SameUserException extends RuntimeException {
    public SameUserException(String message) {
        super(message);
    }
}
