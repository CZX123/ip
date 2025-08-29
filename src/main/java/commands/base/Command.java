package commands.base;

import data.TaskList;
import exceptions.CodyException;
import storage.Storage;
import ui.Ui;

public abstract class Command {
    private final String name;

    protected Command(String name) {
        this.name = name;
    }

    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException;

    public abstract boolean isExit();
}
