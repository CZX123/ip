package commands;

import commands.base.CommandName;
import commands.base.ModifyTaskCommand;
import data.Task;
import data.TaskList;
import exceptions.CodyException;
import exceptions.UserInputException;
import storage.Storage;
import ui.Ui;

public class DeleteCommand extends ModifyTaskCommand {
    public DeleteCommand(int index) {
        super(CommandName.DELETE.getName(), index);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        if (!isIndexValid(tasks, getIndex())) {
            throw new UserInputException(String.format("There is no task numbered %d! \uD83D\uDE35", getIndex() + 1));
        }
        Task task = tasks.get(getIndex());
        tasks.remove(getIndex());
        String result = String.format("\uD83D\uDDD1️ Removed task:\n   %s\n\n\uD83D\uDCCB Now there %s %d task%s!",
                task, tasks.isSingular() ? "is" : "are", tasks.size(), tasks.isSingular() ? "" : "s");
        ui.showCommandResult(result);
        storage.save(tasks);
    }
}
