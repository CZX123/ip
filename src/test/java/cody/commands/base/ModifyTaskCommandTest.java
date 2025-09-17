package cody.commands.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cody.data.TaskList;
import cody.data.Todo;
import cody.storage.Storage;
import cody.ui.Ui;

class ModifyTaskCommandTest {

    private static class ModifyTaskCommandStubOne extends ModifyTaskCommand {
        protected ModifyTaskCommandStubOne(int index) {
            super("same name", index);
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            // No-op for testing purposes
        }
    }

    private static class ModifyTaskCommandStubTwo extends ModifyTaskCommand {
        protected ModifyTaskCommandStubTwo(int index) {
            super("same name", index);
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            // No-op for testing purposes
        }
    }

    @Test
    void testIsExit() {
        ModifyTaskCommand command = new ModifyTaskCommandStubOne(0);
        assertFalse(command.isExit(), "isExit should return false for ModifyTaskCommand");
    }

    @Test
    void testGetIndex() {
        int index = 2;
        ModifyTaskCommand command = new ModifyTaskCommandStubOne(index);
        assertEquals(index, command.getIndex(), "Index should match the provided value");
    }

    @Test
    void testIsIndexInvalid() {
        ModifyTaskCommand commandWithNegativeIndex = new ModifyTaskCommandStubOne(-1);
        assertTrue(commandWithNegativeIndex.isIndexInvalid(null), "Negative index should be invalid");

        TaskList tasks = new TaskList();
        ModifyTaskCommand command = new ModifyTaskCommandStubOne(0);
        // Since the task list is empty, index 0 is invalid
        assertTrue(command.isIndexInvalid(tasks), "Index 0 should be invalid for an empty task list");

        tasks.add(new Todo("Sample Task"));
        // Now that there's one task, index 0 should be valid
        assertFalse(command.isIndexInvalid(tasks), "Index 0 should be valid when there is one task");

        // Index 1 should still be invalid
        ModifyTaskCommand commandWithInvalidIndex = new ModifyTaskCommandStubOne(1);
        assertTrue(commandWithInvalidIndex.isIndexInvalid(tasks),
                "Index 1 should be invalid when there is only one task");
    }

    @Test
    void testEquals() {
        ModifyTaskCommand command1 = new ModifyTaskCommandStubOne(0);
        ModifyTaskCommand command2 = new ModifyTaskCommandStubOne(0);
        ModifyTaskCommand command3 = new ModifyTaskCommandStubOne(1);
        ModifyTaskCommand command4 = new ModifyTaskCommandStubTwo(0);

        assertEquals(command1, command2, "Commands with the same name and index should be equal");
        assertNotEquals(command1, command3, "Commands with different indices should not be equal");
        assertNotEquals(command1, command4, "Commands with different subclasses should not be equal");
    }

    @Test
    void testHashCode() {
        ModifyTaskCommand command1 = new ModifyTaskCommandStubOne(0);
        ModifyTaskCommand command2 = new ModifyTaskCommandStubOne(0);

        assertEquals(command1.hashCode(), command2.hashCode(),
                "Hash codes should match for commands with the same index");
    }
}
