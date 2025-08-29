package commands;

import commands.base.Command;
import commands.base.CommandName;
import data.TaskList;
import exceptions.CodyException;
import storage.Storage;
import ui.Ui;

public class ListCommand extends Command {
    public ListCommand() {
        super(CommandName.LIST.getName());
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        String result;
        if (tasks.isEmpty()) {
            result = "You have no tasks saved! \uD83D\uDE0E";
        } else {
            result = String.format("You have %d task%s! \uD83D\uDCAA\uD83D\uDCDD\n%s",
                    tasks.size(), tasks.isSingular() ? "" : "s", tasks);
        }
        ui.showCommandResult(result);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
