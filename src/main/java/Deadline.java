import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private final LocalDateTime by;
    public Deadline(String desc, LocalDateTime by) {
        super(desc);
        this.by = by;
    }

    @Override
    public boolean fallsOn(LocalDate date) {
        return date.isEqual(by.toLocalDate());
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(),
                by.format(DateTimeFormatter.ofPattern("d MMM yyyy h:mma")));
    }

    public static Deadline fromString(String str) throws CodyException {
        if (!str.matches("\\[D]\\[[X| ]] .+ \\(by: .+\\)")) {
            throw new CodyException("Invalid deadline format!");
        }
        String[] split1 = str.split("] ", 2); // "[D][X" & "<desc> (by: <due>)"
        String[] split2 = split1[1].split(" \\(by: ", 2); // "<desc>" and "<due>)"
        LocalDateTime by;
        try {
            by = LocalDateTime.parse(split2[1].substring(0, split2[1].length() - 1),
                    DateTimeFormatter.ofPattern("d MMM yyyy h:mma"));
        } catch (DateTimeParseException e) {
            throw new CodyException("Invalid deadline format!");
        }
        Deadline deadline = new Deadline(split2[0], by);
        if (split1[0].charAt(4) == 'X') {
            deadline.markDone();
        }
        return deadline;
    }

    public static Deadline fromCommand(String cmd) throws CodyException {
        if (!cmd.matches("deadline .+ /by .+")) {
            throw new CodyException("Deadlines should follow this format:\n"
                    + Cody.INDENT + "deadline <description> /by YYYY-MM-DD HHmm");
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
