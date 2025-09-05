package cody.commands.base;

import java.util.Objects;

import cody.data.TaskList;
import cody.exceptions.CodyException;

/**
 * Represents an executable command
 */
public abstract class Command {
    private final String name;

    protected Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Executes the command.
     *
     * @param tasks the active {@code TaskList}
     * @throws CodyException on any invalid user input or storage operation error
     */
    public abstract void execute(TaskList tasks) throws CodyException;

    /**
     * Returns whether command is an exit command.
     */
    public abstract boolean isExit();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Command command = (Command) o;
        return Objects.equals(name, command.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
