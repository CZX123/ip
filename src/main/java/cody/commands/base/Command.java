package cody.commands.base;

import cody.commands.DeadlineCommand;
import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.storage.Storage;
import cody.ui.Ui;

import java.util.Objects;

public abstract class Command {
    private final String name;

    protected Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException;

    public abstract boolean isExit();

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Command)
                && Objects.equals(name, ((Command) other).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
