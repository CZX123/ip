package cody.exceptions;

/**
 * Exception that results from errors during saving or loading files from storage.
 */
public class StorageOperationException extends CodyException {
    public StorageOperationException(String message) {
        super(message);
    }
}
