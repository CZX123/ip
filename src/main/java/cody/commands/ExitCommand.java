package cody.commands;

import cody.commands.base.Command;
import cody.commands.base.CommandName;
import cody.data.TaskList;
import cody.storage.Storage;
import cody.ui.Ui;

/**
 * Quits the program.
 */
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
