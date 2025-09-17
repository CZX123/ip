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
 * Mark task as done based on its index.
 */
public class MarkCommand extends ModifyTaskCommand {
    public MarkCommand(int index) {
        super(CommandName.MARK.getName(), index);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        if (isIndexInvalid(tasks, getIndex())) {
            throw new UserInputException(String.format("There is no task numbered %d!", getIndex() + 1));
        }
        Task task = tasks.get(getIndex());
        task.markDone();
        ui.showCodyResponse("Marked task as done:\n" + task);
        storage.save(tasks);
    }
}
