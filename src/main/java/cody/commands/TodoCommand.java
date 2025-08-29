package cody.commands;

import cody.commands.base.AddTaskCommand;
import cody.commands.base.CommandName;
import cody.data.Task;
import cody.data.Todo;

public class TodoCommand extends AddTaskCommand {
    public TodoCommand(String description) {
        super(CommandName.TODO.getName(), description);
    }

    @Override
    public Task createTask() {
        return new Todo(getDescription());
    }
}
