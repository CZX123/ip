package commands;

import commands.base.Command;
import commands.base.CommandName;
import data.TaskList;
import exceptions.CodyException;
import storage.Storage;
import ui.Ui;

public class ExitCommand extends Command {
    public ExitCommand() {
        super(CommandName.EXIT.getName());
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // do nothing
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
