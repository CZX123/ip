package cody.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;
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
        return 'E';
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
}
