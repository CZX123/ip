package commands;

import commands.base.AddTaskCommand;
import commands.base.CommandName;
import data.Deadline;
import data.Task;

import java.time.LocalDateTime;

public class DeadlineCommand extends AddTaskCommand {
    private final LocalDateTime by;

    public DeadlineCommand(String description, LocalDateTime by) {
        super(CommandName.DEADLINE.getName(), description);
        this.by = by;
    }

    @Override
    public Task createTask() {
        return new Deadline(getDescription(), by);
    }
}
