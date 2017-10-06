package cz.tyckouni.ticketchecker.core;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class InvalidStateException extends RuntimeException {
    public InvalidStateException() {
    }

    public InvalidStateException(String message) {
        super(message);
    }

    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidStateException(Throwable cause) {
        super(cause);
    }

    public InvalidStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
