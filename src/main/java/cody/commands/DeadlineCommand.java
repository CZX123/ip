package cody.commands;

import cody.commands.base.AddTaskCommand;
import cody.commands.base.CommandName;
import cody.data.Deadline;
import cody.data.Task;

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
