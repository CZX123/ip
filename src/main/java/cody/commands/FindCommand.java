package cody.commands;

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
     * @param keyword the search keyword
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
                            ui.removeNumberingFromTasks(filteredTasks.toString())));
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
