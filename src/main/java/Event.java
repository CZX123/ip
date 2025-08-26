public class Event extends Task {
    private final String from;
    private final String to;
    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), from, to);
    }

    public static Event fromString(String str) throws CodyException {
        if (!str.matches("\\[E]\\[[X| ]] .+ \\(from: .+ to: .+\\)")) {
            throw new CodyException("Invalid event format!");
        }
        String[] split1 = str.split("] ", 2); // "[E][X" & "<desc> (from: <start> to: <end>)"
        String[] split2 = split1[1].split(" \\(from: ", 2); // "<desc>" & "<start> to: <end>)"
        String[] split3 = split2[1].split(" to: ", 2); // "<start>" & "<end>)"
        Event event = new Event(split2[0], split3[0], split3[1].substring(0, split3[1].length() - 1));
        if (split1[0].charAt(4) == 'X') {
            event.markDone();
        }
        return event;
    }

    public static Event fromCommand(String cmd) throws CodyException {
        if (!cmd.matches("event .+ /from .+ /to .+")) {
            throw new CodyException("Events should follow this format:\n"
                    + Cody.INDENT + "event <description> /from <start date> /to <end date>");
        }
        String[] fromSplit = cmd.split(" ", 2)[1].split(" /from ", 2);
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        return new Event(fromSplit[0], toSplit[0], toSplit[1]);
    }
}
