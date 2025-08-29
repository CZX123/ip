package commands.base;

import data.Task;
import data.TaskList;
import exceptions.CodyException;
import storage.Storage;
import ui.Ui;

public abstract class AddTaskCommand extends Command {
    private final String description;

    public AddTaskCommand(String name, String description) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract Task createTask();

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CodyException {
        Task task = createTask();
        tasks.add(task);
        String result = String.format("➕ Added task:\n   %s\n\n\uD83D\uDCCB Now there %s %d task%s!",
                task, tasks.isSingular() ? "is" : "are", tasks.size(), tasks.isSingular() ? "" : "s");
        ui.showCommandResult(result);
        storage.save(tasks);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
