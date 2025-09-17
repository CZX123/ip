package cody.testutil;

import java.time.LocalDate;

import cody.data.Task;

/**
 * A stub class of {@code Task} to be used in testing.
 */
public class TaskStub extends Task {
    private final boolean fallsOn;

    /**
     * Constructs a TaskStub with the given description, with fallsOn set to false.
     *
     * @param description The task description.
     */
    public TaskStub(String description) {
        this(description, false);
    }

    /**
     * Constructs a TaskStub with the given description and fallsOn behavior.
     *
     * @param description The task description.
     * @param fallsOn Whether the task falls on a specific date.
     */
    public TaskStub(String description, boolean fallsOn) {
        super(description);
        this.fallsOn = fallsOn;
    }

    @Override
    public char getLetter() {
        return 0;
    }

    @Override
    public boolean fallsOn(LocalDate date) {
        return fallsOn;
    }

    /**
     * Returns "[Stub] " followed by the task description.
     */
    @Override
    public String toString() {
        return "[Stub] " + getDescription();
    }
}
