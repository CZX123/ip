package cody.commands.base;

import java.util.Objects;

import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.storage.Storage;
import cody.ui.Ui;

/**
 * Represents an executable command.
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
     * @param tasks   the active {@code TaskList} instance.
     * @param ui      the active {@code Ui} instance.
     * @param storage the active {@code Storage} instance.
     * @throws CodyException on any invalid user input or storage operation error.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException;

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

    /**
     * Returns a string representation of the command.
     * Only used for testing and debugging.
     */
    @Override
    public String toString() {
        return String.format("%s{name='%s'}", getClass().getSimpleName(), name);
    }
}
