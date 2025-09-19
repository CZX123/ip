package cody.commands;

import java.time.LocalDateTime;
import java.util.Objects;

import cody.commands.base.AddTaskCommand;
import cody.commands.base.CommandName;
import cody.data.Event;
import cody.data.Task;

/**
 * Adds an event to the task list.
 */
public class EventCommand extends AddTaskCommand {
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs an event command.
     *
     * @param description event description.
     * @param from event start date and time.
     * @param to event end date and time.
     */
    public EventCommand(String description, LocalDateTime from, LocalDateTime to) {
        super(CommandName.DEADLINE.getName(), description);
        this.from = from;
        this.to = to;
    }

    @Override
    protected Task createTask() {
        return new Event(getDescription(), from, to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EventCommand that = (EventCommand) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to);
    }

    @Override
    public String toString() {
        return String.format("%s, from=%s, to=%s}",
                super.toString().substring(0, super.toString().length() - 1), from, to);
    }
}
