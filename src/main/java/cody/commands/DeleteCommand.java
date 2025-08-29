package cody.commands;

import cody.commands.base.CommandName;
import cody.commands.base.ModifyTaskCommand;
import cody.data.Task;
import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.exceptions.UserInputException;
import cody.storage.Storage;
import cody.ui.Ui;

public class DeleteCommand extends ModifyTaskCommand {
    public DeleteCommand(int index) {
        super(CommandName.DELETE.getName(), index);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        if (isIndexInvalid(tasks, getIndex())) {
            throw new UserInputException(String.format("There is no task numbered %d! \uD83D\uDE35", getIndex() + 1));
        }
        Task task = tasks.get(getIndex());
        tasks.remove(getIndex());
        String result = String.format("\uD83D\uDDD1\uFE0F Removed task:\n   %s\n\n\uD83D\uDCCB Now there %s %d task%s!",
                task, tasks.isSingular() ? "is" : "are", tasks.size(), tasks.isSingular() ? "" : "s");
        ui.showCommandResult(result);
        storage.save(tasks);
    }
}
