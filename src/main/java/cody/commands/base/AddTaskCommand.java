package cody.commands.base;

import java.util.Objects;

import cody.data.Task;
import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.storage.Storage;
import cody.ui.Ui;

/**
 * Adds a task (i.e. todos, deadlines, events) to the task list.
 */
public abstract class AddTaskCommand extends Command {
    private final String description;

    protected AddTaskCommand(String name, String description) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    protected abstract Task createTask();

    @Override
    public void execute(TaskList tasks) throws CodyException {
        Task task = createTask();
        tasks.add(task);
        String result = String.format("Added task:\n%s\n\nNow there %s %d task%s!",
                task, tasks.isSingular() ? "is" : "are", tasks.size(), tasks.isSingular() ? "" : "s");
        Ui.getInstance().showCodyResponse(result);
        Storage.getInstance().save(tasks);
    }

    @Override
    public boolean isExit() {
        return false;
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
        AddTaskCommand that = (AddTaskCommand) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }
}
