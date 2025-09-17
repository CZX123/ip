package cody.commands.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.testutil.StorageStub;
import cody.testutil.TaskStub;
import cody.testutil.UiStub;

class AddTaskCommandTest {

    @Test
    void testGetDescription() {
        String description = "Sample task";
        AddTaskCommandStub command = new AddTaskCommandStub(description);
        assertEquals(description, command.getDescription(), "Descriptions should match");
    }

    @Test
    void testIsExit() {
        AddTaskCommandStub command = new AddTaskCommandStub("Sample task");
        assertFalse(command.isExit(), "isExit should return false for AddTaskCommand");
    }

    @Test
    void execute_emptyTaskList_taskAddedCorrectly() throws CodyException {
        TaskList tasks = new TaskList();
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        AddTaskCommandStub command = new AddTaskCommandStub("Sample task");
        String expectedResponses = "Added task:\n[Stub] Sample task\n\nNow there is 1 task!";

        command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size(), "Task list size should be 1 after adding a task");
        assertEquals(new TaskStub("Sample task"), tasks.get(0), "The added task should match the created TaskStub");
        assertEquals(expectedResponses, ui.getCodyResponses(), "UI response should match expected format");
        assertEquals(1, storage.getSaveCallCount(), "Storage save should be called once");
    }

    @Test
    void execute_nonEmptyTaskList_taskAddedCorrectly() throws CodyException {
        TaskList tasks = new TaskList(new TaskStub("Existing task"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        AddTaskCommandStub command = new AddTaskCommandStub("Sample task");
        String expectedResponses = "Added task:\n[Stub] Sample task\n\nNow there are 2 tasks!";

        command.execute(tasks, ui, storage);

        assertEquals(2, tasks.size(), "Task list size should be 2 after adding a task");
        assertEquals(new TaskStub("Sample task"), tasks.get(1), "The added task should match the created TaskStub");
        assertEquals(expectedResponses, ui.getCodyResponses(), "UI response should match expected format");
        assertEquals(1, storage.getSaveCallCount(), "Storage save should be called once");
    }

    @Test
    void testEquals() {
        AddTaskCommandStub command1 = new AddTaskCommandStub("Task 1");
        AddTaskCommandStub command2 = new AddTaskCommandStub("Task 1");
        AddTaskCommandStub command3 = new AddTaskCommandStub("Task 2");

        assertEquals(command1, command2, "Commands with the same description should be equal");
        assertNotEquals(command1, command3, "Commands with different descriptions should not be equal");
    }

    @Test
    void testHashCode() {
        AddTaskCommandStub command1 = new AddTaskCommandStub("Task 1");
        AddTaskCommandStub command2 = new AddTaskCommandStub("Task 1");

        assertEquals(command1.hashCode(), command2.hashCode(), "Hash codes should match for same descriptions");
    }

    // Stub class for AddTaskCommand
    private static class AddTaskCommandStub extends AddTaskCommand {
        protected AddTaskCommandStub(String description) {
            super("add", description);
        }

        @Override
        protected TaskStub createTask() {
            return new TaskStub(getDescription());
        }
    }
}
