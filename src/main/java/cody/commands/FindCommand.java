package cody.commands;

import java.util.Objects;

import cody.commands.base.Command;
import cody.commands.base.CommandName;
import cody.data.TaskList;
import cody.storage.Storage;
import cody.ui.Ui;

/**
 * Finds the task that has the given keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a find command with the given keyword.
     *
     * @param keyword the search keyword.
     */
    public FindCommand(String keyword) {
        super(CommandName.FIND.getName());
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        TaskList filteredTasks = tasks.filter(task -> task.getDescription().contains(keyword));
        if (filteredTasks.isEmpty()) {
            ui.showCodyResponse("There are no tasks that match \"" + keyword + "\"");
        } else {
            ui.showCodyResponse(
                    String.format("There %s %d matching task%s:\n%s", filteredTasks.isSingular() ? "is" : "are",
                            filteredTasks.size(), filteredTasks.isSingular() ? "" : "s",
                            filteredTasks.toStringWithoutNumbering()));
        }
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
        FindCommand that = (FindCommand) o;
        return Objects.equals(keyword, that.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), keyword);
    }

    @Override
    public String toString() {
        return String.format("%s, keyword='%s'}",
                super.toString().substring(0, super.toString().length() - 1), keyword);
    }
}
