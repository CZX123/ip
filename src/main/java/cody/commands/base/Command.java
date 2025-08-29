package cody.commands.base;

import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.storage.Storage;
import cody.ui.Ui;

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
}
