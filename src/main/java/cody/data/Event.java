package cody.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents an event.
 */
public class Event extends Task {
    /**
     * Used to denote task type when storing task in plaintext.
     */
    public static final char LETTER = 'E';

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs an event based on the given description, start date and end date.
     *
     * @param description event description
     * @param from event start date
     * @param to event end date
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public char getLetter() {
        return LETTER;
    }

    @Override
    public boolean fallsOn(LocalDate date) {
        return !date.isBefore(from.toLocalDate()) && !date.isAfter(to.toLocalDate());
    }

    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)", super.toString(),
                from.format(DateTimeFormatter.ofPattern("d MMM yyyy h:mma")),
                to.format(DateTimeFormatter.ofPattern("d MMM yyyy h:mma")));
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
        Event event = (Event) o;
        return Objects.equals(from, event.from) && Objects.equals(to, event.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to);
    }
}
