package mate.academy.bookstore.exception;

public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataProcessingException(String message) {
        super(message);
    }
}
