package cody.commands;

import cody.commands.base.AddTaskCommand;
import cody.commands.base.CommandName;
import cody.data.Deadline;
import cody.data.Task;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeadlineCommand)
                && super.equals(other) && Objects.equals(by, ((DeadlineCommand) other).by);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), by);
    }
}
