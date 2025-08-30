package cody.commands.base;

/**
 * Name of the command
 */
public enum CommandName {
    BYE("bye"),
    EXIT("exit"),
    LIST("list"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    FIND("find");

    private final String name;

    CommandName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
