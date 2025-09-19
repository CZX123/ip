package cody.data;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a task.
 */
public abstract class Task {
    private final String description;
    private boolean isDone = false;

    /**
     * Constructs a task with the given description.
     *
     * @param description text that describes the task.
     */
    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmarkDone() {
        isDone = false;
    }

    /**
     * Returns whether task is done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the character that represents the task type.
     * Used when storing the task in plaintext.
     */
    public abstract char getLetter();

    /**
     * Returns whether the task falls on the given date.
     */
    public abstract boolean fallsOn(LocalDate date);

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", getLetter(), isDone ? "X" : " ", description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return isDone == task.isDone && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, isDone);
    }
}
