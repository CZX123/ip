package cody.commands;

import java.time.LocalDateTime;
import java.util.Objects;

import cody.commands.base.AddTaskCommand;
import cody.commands.base.CommandName;
import cody.data.Deadline;
import cody.data.Task;

/**
 * Adds a deadline to the task list.
 */
public class DeadlineCommand extends AddTaskCommand {
    private final LocalDateTime by;

    /**
     * Constructs a deadline command
     *
     * @param description deadline description
     * @param by deadline due date and time
     */
    public DeadlineCommand(String description, LocalDateTime by) {
        super(CommandName.DEADLINE.getName(), description);
        this.by = by;
    }

    @Override
    protected Task createTask() {
        return new Deadline(getDescription(), by);
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
        DeadlineCommand that = (DeadlineCommand) o;
        return Objects.equals(by, that.by);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), by);
    }
}
