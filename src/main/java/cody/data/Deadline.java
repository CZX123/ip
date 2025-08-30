package cody.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a deadline.
 */
public class Deadline extends Task {
    private final LocalDateTime by;

    /**
     * Constructs a deadline based on the given description and due date.
     *
     * @param description deadline description
     * @param by deadline due date
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public char getLetter() {
        return 'D';
    }

    @Override
    public boolean fallsOn(LocalDate date) {
        return date.isEqual(by.toLocalDate());
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(),
                by.format(DateTimeFormatter.ofPattern("d MMM yyyy h:mma")));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Deadline deadline = (Deadline) o;
        return Objects.equals(by, deadline.by);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), by);
    }
}
