package commands;

import commands.base.CommandName;
import commands.base.ModifyTaskCommand;
import data.Task;
import data.TaskList;
import exceptions.CodyException;
import exceptions.UserInputException;
import storage.Storage;
import ui.Ui;

public class UnmarkCommand extends ModifyTaskCommand {
    public UnmarkCommand(int index) {
        super(CommandName.UNMARK.getName(), index);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        if (!isIndexValid(tasks, getIndex())) {
            throw new UserInputException(String.format("There is no task numbered %d! \uD83D\uDE35", getIndex() + 1));
        }
        Task task = tasks.get(getIndex());
        task.unmarkDone();
        ui.showCommandResult("↩\uFE0F Marked task as not done:\n   " + task);
        storage.save(tasks);
    }
}
