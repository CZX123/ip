public class Todo extends Task {
    public Todo(String desc) { super(desc); }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    public static Todo fromString(String str) throws CodyException {
        if (!str.matches("\\[T]\\[[X| ]] .+")) {
            throw new CodyException("Invalid todo format!");
        }
        String[] split = str.split("] ", 2); // "[T][X" & "<desc>"
        Todo todo = new Todo(split[1]);
        if (split[0].charAt(4) == 'X') {
            todo.markDone();
        }
        return todo;
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
