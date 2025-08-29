package cody.commands;

import cody.commands.base.AddTaskCommand;
import cody.commands.base.CommandName;
import cody.data.Event;
import cody.data.Task;

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
