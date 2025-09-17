package cody.commands.base;

import java.util.Objects;

import cody.data.TaskList;

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

    /**
     * Checks if the index is invalid for the given task list.
     *
     * @param tasks the active {@code TaskList} instance
     * @return whether the index is out of bounds of the active task list.
     */
    protected boolean isIndexInvalid(TaskList tasks) {
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
