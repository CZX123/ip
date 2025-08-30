package cody.exceptions;

/**
 * Exception that results from invalid user input.
 */
public class UserInputException extends CodyException {
    public UserInputException(String message) {
        super(message);
    }
}
