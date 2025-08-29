package commands;

import commands.base.AddTaskCommand;
import commands.base.CommandName;
import data.Task;
import data.Todo;

public class TodoCommand extends AddTaskCommand {
    public TodoCommand(String description) {
        super(CommandName.TODO.getName(), description);
    }

    @Override
    public Task createTask() {
        return new Todo(getDescription());
    }
}
