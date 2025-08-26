public class Deadline extends Task {
    private final String by;
    public Deadline(String desc, String by) {
        super(desc);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), by);
    }

    public static Deadline fromString(String str) throws CodyException {
        if (!str.matches("\\[D]\\[[X| ]] .+ (by: .+)")) {
            throw new CodyException("Invalid deadline format!");
        }
        String[] split1 = str.split("] ", 2); // "[D][X" & "<desc> (by: <due>)"
        String[] split2 = split1[1].split(" \\(by: ", 2); // "<desc>" and "<due>)"
        Deadline deadline = new Deadline(split2[0], split2[1].substring(0, split2[1].length() - 1));
        if (split1[0].charAt(4) == 'X') {
            deadline.markDone();
        }
        return deadline;
    }

    public static Deadline fromCommand(String cmd) throws CodyException {
        if (!cmd.matches("deadline .+ /by .+")) {
            throw new CodyException("Deadlines should follow this format:\n"
                    + Cody.INDENT + "deadline <description> /by <due date>");
        }
        String[] split = cmd.split(" ", 2)[1].split(" /by ", 2);
        return new Deadline(split[0], split[1]);
    }
}
