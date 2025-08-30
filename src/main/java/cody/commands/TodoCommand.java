package cody.commands;

import cody.commands.base.AddTaskCommand;
import cody.commands.base.CommandName;
import cody.data.Task;
import cody.data.Todo;

/**
 * Adds a todo to the task list.
 */
public class TodoCommand extends AddTaskCommand {
    public TodoCommand(String description) {
        super(CommandName.TODO.getName(), description);
    }

    @Override
    protected Task createTask() {
        return new Todo(getDescription());
    }
}
