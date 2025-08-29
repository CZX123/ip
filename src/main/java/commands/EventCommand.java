package commands;

import commands.base.AddTaskCommand;
import commands.base.CommandName;
import data.Event;
import data.Task;

import java.time.LocalDateTime;

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
}
