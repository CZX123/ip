package cody.data;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Task {
    private final String description;
    private boolean isDone = false;

    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void markDone() {
        isDone = true;
    }

    public void unmarkDone() {
        isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public abstract char getLetter();

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
