package commands;

import commands.base.CommandName;
import commands.base.ModifyTaskCommand;
import data.Task;
import data.TaskList;
import exceptions.CodyException;
import exceptions.UserInputException;
import storage.Storage;
import ui.Ui;

public class MarkCommand extends ModifyTaskCommand {
    public MarkCommand(int index) {
        super(CommandName.MARK.getName(), index);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        if (!isIndexValid(tasks, getIndex())) {
            throw new UserInputException(String.format("There is no task numbered %d! \uD83D\uDE35", getIndex() + 1));
        }
        Task task = tasks.get(getIndex());
        task.markDone();
        ui.showCommandResult("✅ Marked task as done:\n   " + task);
        storage.save(tasks);
    }
}
