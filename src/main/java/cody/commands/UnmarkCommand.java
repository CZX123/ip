package cody.commands;

import cody.commands.base.CommandName;
import cody.commands.base.ModifyTaskCommand;
import cody.data.Task;
import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.exceptions.UserInputException;
import cody.storage.Storage;
import cody.ui.Ui;

/**
 * Marks task as not done based on its index.
 */
public class UnmarkCommand extends ModifyTaskCommand {
    public UnmarkCommand(int index) {
        super(CommandName.UNMARK.getName(), index);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        if (isIndexInvalid(tasks)) {
            throw new UserInputException(String.format("There is no task numbered %d!", getIndex() + 1));
        }
        Task task = tasks.get(getIndex());
        task.unmarkDone();
        ui.showCodyResponse("Marked task as not done:\n" + task);
        storage.save(tasks);
    }
}
