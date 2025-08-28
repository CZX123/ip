import java.time.LocalDate;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public char getLetter() {
        return 'T';
    }

    @Override
    public boolean fallsOn(LocalDate date) {
        return false;
    }

    public static Todo fromCommand(String cmd) throws CodyException {
        String[] split = cmd.split(" ", 2);
        String command = split[0];
        if (!command.equals("todo")) {
            throw new CodyException("⚠\uFE0F Invalid command!");
        }
        if (split.length < 2) {
            throw new CodyException("The description of a todo cannot be empty!");
        }
        return new Todo(split[1]);
    }
}
