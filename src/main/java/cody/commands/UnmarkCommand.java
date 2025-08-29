package cody.commands;

import cody.commands.base.CommandName;
import cody.commands.base.ModifyTaskCommand;
import cody.data.Task;
import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.exceptions.UserInputException;
import cody.storage.Storage;
import cody.ui.Ui;

public class UnmarkCommand extends ModifyTaskCommand {
    public UnmarkCommand(int index) {
        super(CommandName.UNMARK.getName(), index);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        if (isIndexInvalid(tasks, getIndex())) {
            throw new UserInputException(String.format("There is no task numbered %d! \uD83D\uDE35", getIndex() + 1));
        }
        Task task = tasks.get(getIndex());
        task.unmarkDone();
        ui.showCommandResult("↩\uFE0F Marked task as not done:\n   " + task);
        storage.save(tasks);
    }
}
