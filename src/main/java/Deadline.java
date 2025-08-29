import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public static Deadline fromCommand(String cmd) throws CodyException {
        if (!cmd.matches("deadline .+ /by .+")) {
            throw new CodyException("Deadlines should follow this format:\n"
                    + "deadline <description> /by YYYY-MM-DD HHmm");
        }
        String[] split = cmd.split(" ", 2)[1].split(" /by ", 2);
        try {
            LocalDateTime by = LocalDateTime.parse(split[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            return new Deadline(split[0], by);
        } catch (DateTimeParseException e) {
            throw new CodyException("The due date should be in this format: YYYY-MM-DD HHmm\n");
        }
    }
}
