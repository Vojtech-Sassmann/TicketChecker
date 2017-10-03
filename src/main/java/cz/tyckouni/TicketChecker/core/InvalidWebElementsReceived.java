package cz.tyckouni.TicketChecker.core;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class InvalidWebElementsReceived extends Exception {
    public InvalidWebElementsReceived() {
        super();
    }

    public InvalidWebElementsReceived(String message) {
        super(message);
    }

    public InvalidWebElementsReceived(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidWebElementsReceived(Throwable cause) {
        super(cause);
    }

    protected InvalidWebElementsReceived(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
