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
 * Deletes a task based on its id.
 */
public class DeleteCommand extends ModifyTaskCommand {
    public DeleteCommand(int index) {
        super(CommandName.DELETE.getName(), index);
    }

    @Override
    public void execute(TaskList tasks) throws CodyException {
        if (isIndexInvalid(tasks, getIndex())) {
            throw new UserInputException(String.format("There is no task numbered %d!", getIndex() + 1));
        }
        Task task = tasks.get(getIndex());
        tasks.remove(getIndex());
        String result = String.format("Removed task:\n%s\n\nNow there %s %d task%s!",
                task, tasks.isSingular() ? "is" : "are", tasks.size(), tasks.isSingular() ? "" : "s");
        Ui.getInstance().showCodyResponse(result);
        Storage.getInstance().save(tasks);
    }
}
