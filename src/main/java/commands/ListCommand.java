package commands;

import commands.base.Command;
import commands.base.CommandName;
import data.TaskList;
import storage.Storage;
import ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ListCommand extends Command {
    private final LocalDate date;

    public ListCommand() {
        super(CommandName.LIST.getName());
        date = null;
    }

    public ListCommand(LocalDate date) {
        super(CommandName.LIST.getName());
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String result;
        if (date == null) {
            if (tasks.isEmpty()) {
                result = "You have no tasks saved! \uD83D\uDE0E";
            } else {
                result = String.format("You have %d task%s! \uD83D\uDCAA\uD83D\uDCDD\n%s",
                        tasks.size(), tasks.isSingular() ? "" : "s", tasks);
            }
        } else {
            TaskList filteredTasks = tasks.filter(task -> task.fallsOn(date));
            DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMM yyyy");
            if (filteredTasks.isEmpty()) {
                result = String.format("You have no tasks on %s! \uD83D\uDE0E", date.format(format));
            } else {
                result = String.format("You have %d task%s on %s! \uD83D\uDCAA\uD83D\uDCDD\n%s",
                        filteredTasks.size(), filteredTasks.isSingular() ? "" : "s", date.format(format),
                                ui.removeNumberingFromTasks(filteredTasks.toString()));
            }
        }
        ui.showCommandResult(result);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
