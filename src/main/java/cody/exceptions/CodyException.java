package cody.exceptions;

/**
 * General checked exception used in the application.
 * Error message is intended to be displayed to the user.
 */
public class CodyException extends Exception {
    public CodyException(String message) {
        super(message);
    }
}
