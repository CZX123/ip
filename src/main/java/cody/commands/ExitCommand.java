package cody.commands;

import cody.commands.base.Command;
import cody.commands.base.CommandName;
import cody.data.TaskList;

/**
 * Quits the program.
 */
public class ExitCommand extends Command {
    public ExitCommand() {
        super(CommandName.EXIT.getName());
    }

    @Override
    public void execute(TaskList tasks) {
        // do nothing
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
