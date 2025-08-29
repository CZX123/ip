package commands.base;

import data.TaskList;

public abstract class ModifyTaskCommand extends Command {
    private final int index;

    protected ModifyTaskCommand(String name, int index) {
        super(name);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean isIndexValid(TaskList tasks, int index) {
        return 0 <= index && index < tasks.size();
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
