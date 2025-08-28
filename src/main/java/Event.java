import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public static Event fromString(String str) throws CodyException {
        if (!str.matches("\\[E]\\[[X| ]] .+ \\(from: .+ to: .+\\)")) {
            throw new CodyException("Invalid event format!");
        }
        String[] split1 = str.split("] ", 2); // "[E][X" & "<desc> (from: <start> to: <end>)"
        String[] split2 = split1[1].split(" \\(from: ", 2); // "<desc>" & "<start> to: <end>)"
        String[] split3 = split2[1].split(" to: ", 2); // "<start>" & "<end>)"
        LocalDateTime from, to;
        try {
            from = LocalDateTime.parse(split3[0], DateTimeFormatter.ofPattern("d MMM yyyy h:mma"));
            to = LocalDateTime.parse(split3[1].substring(0, split3[1].length() - 1),
                    DateTimeFormatter.ofPattern("d MMM yyyy h:mma"));
        } catch (DateTimeParseException e) {
            throw new CodyException("Invalid event format!");
        }
        Event event = new Event(split2[0], from,  to);
        if (split1[0].charAt(4) == 'X') {
            event.markDone();
        }
        return event;
    }

    public static Event fromCommand(String cmd) throws CodyException {
        if (!cmd.matches("event .+ /from .+ /to .+")) {
            throw new CodyException("Events should follow this format:\n"
                    + Cody.INDENT + "event <description> /from YYYY-MM-DD HHmm /to YYYY-MM-DD HHmm");
        }
        String[] fromSplit = cmd.split(" ", 2)[1].split(" /from ", 2);
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        try {
            LocalDateTime from = LocalDateTime.parse(toSplit[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            LocalDateTime to = LocalDateTime.parse(toSplit[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            return new Event(fromSplit[0], from, to);
        } catch (DateTimeParseException e) {
            throw new CodyException("The dates should be in this format: YYYY-MM-DD HHmm\n");
        }
    }
}
