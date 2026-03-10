package Exception;

public class BusException extends Exception {
    public BusException(String message, Throwable cause) {
        super(message, cause); // Gọi constructor của Exception với message và cause
    }
    public BusException(String message) {
        super(message); // Gọi constructor của Exception với message
    }
}