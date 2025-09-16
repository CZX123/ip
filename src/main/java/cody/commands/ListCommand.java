package cody.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import cody.commands.base.Command;
import cody.commands.base.CommandName;
import cody.data.TaskList;
import cody.ui.Ui;

/**
 * Lists all tasks in the task list.
 * If date is provided, lists tasks on that date instead.
 */
public class ListCommand extends Command {
    private final LocalDate date;

    /**
     * Constructs a list command that lists all tasks in the task list.
     */
    public ListCommand() {
        super(CommandName.LIST.getName());
        date = null;
    }

    /**
     * Constructs a list command that lists all tasks on the given date.
     *
     * @param date the date that filters the tasks
     */
    public ListCommand(LocalDate date) {
        super(CommandName.LIST.getName());
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks) {
        String result;
        if (date == null) {
            if (tasks.isEmpty()) {
                result = "You have no tasks saved!";
            } else {
                result = String.format("You have %d task%s!\n%s",
                        tasks.size(), tasks.isSingular() ? "" : "s", tasks);
            }
        } else {
            TaskList filteredTasks = tasks.filter(task -> task.fallsOn(date));
            DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMM yyyy");
            if (filteredTasks.isEmpty()) {
                result = String.format("You have no tasks on %s!", date.format(format));
            } else {
                result = String.format("You have %d task%s on %s!\n%s",
                        filteredTasks.size(), filteredTasks.isSingular() ? "" : "s", date.format(format),
                                Ui.getInstance().removeNumberingFromTasks(filteredTasks.toString()));
            }
        }
        Ui.getInstance().showCodyResponse(result);
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
        ListCommand that = (ListCommand) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }
}
