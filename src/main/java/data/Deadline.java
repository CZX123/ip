package data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDateTime by;
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
}
