package cody.commands.base;

import cody.data.TaskList;

import java.util.Objects;

/**
 * Modifies task based on its command and index.
 */
public abstract class ModifyTaskCommand extends Command {
    private final int index;

    protected ModifyTaskCommand(String name, int index) {
        super(name);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    protected boolean isIndexInvalid(TaskList tasks, int index) {
        return 0 > index || index >= tasks.size();
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
        ModifyTaskCommand that = (ModifyTaskCommand) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), index);
    }
}
