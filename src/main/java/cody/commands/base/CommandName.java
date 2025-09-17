package cody.commands.base;

/**
 * Represents the name of the command.
 */
public enum CommandName {
    BYE,
    EXIT,
    LIST,
    FIND,
    TODO,
    DEADLINE,
    EVENT,
    MARK,
    UNMARK,
    DELETE,
    EDIT,
    UPDATE;

    /**
     * Returns the command name in lowercase.
     */
    public String getName() {
        return name().toLowerCase();
    }
}
