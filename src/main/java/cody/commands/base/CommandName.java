package cody.commands.base;

/**
 * Name of the command
 */
public enum CommandName {
    BYE("bye"),
    EXIT("exit"),
    LIST("list"),
    FIND("find"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    EDIT("edit"),
    UPDATE("update");

    private final String name;

    CommandName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
