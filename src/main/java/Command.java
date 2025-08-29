public enum Command {
    BYE("bye"),
    EXIT("exit"),
    LIST("list"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete");
    private final String text;

    Command(String text) {
        this.text = text;
    }

    public static Command of(String input) throws CodyException {
        String firstWord = input.split(" ", 2)[0];
        for (Command command : Command.values()) {
            if (firstWord.equals(command.text)) {
                return command;
            }
        }
        throw new CodyException("⚠\uFE0F Invalid command!");
    }
}
