package data;

import java.time.LocalDate;

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
}
