package cody.commands.base;

import cody.data.TaskList;

import java.util.Objects;

public abstract class ModifyTaskCommand extends Command {
    private final int index;

    protected ModifyTaskCommand(String name, int index) {
        super(name);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean isIndexInvalid(TaskList tasks, int index) {
        return 0 > index || index >= tasks.size();
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ModifyTaskCommand)
                && super.equals(other) && Objects.equals(index, ((ModifyTaskCommand) other).index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), index);
    }
}
