package cody.data;

import java.time.LocalDate;

/**
 * Represents a todo.
 */
public class Todo extends Task {

    /**
     * Used to denote task type when storing task in plaintext.
     */
    public static final char LETTER = 'T';

    /**
     * Constructs a todo based on the given description.
     *
     * @param description todo description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public char getLetter() {
        return LETTER;
    }

    @Override
    public boolean fallsOn(LocalDate date) {
        return false;
    }
}
