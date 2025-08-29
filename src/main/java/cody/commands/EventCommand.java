package cody.commands;

import cody.commands.base.AddTaskCommand;
import cody.commands.base.CommandName;
import cody.data.Event;
import cody.data.Task;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventCommand extends AddTaskCommand {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public EventCommand(String description, LocalDateTime from, LocalDateTime to) {
        super(CommandName.DEADLINE.getName(), description);
        this.from = from;
        this.to = to;
    }

    @Override
    public Task createTask() {
        return new Event(getDescription(), from, to);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EventCommand)
                && super.equals(other)
                && Objects.equals(from, ((EventCommand) other).from) && Objects.equals(to, ((EventCommand) other).to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to);
    }
}
