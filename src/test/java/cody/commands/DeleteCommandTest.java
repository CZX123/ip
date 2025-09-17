package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.exceptions.UserInputException;
import cody.testutil.StorageStub;
import cody.testutil.TaskStub;
import cody.testutil.UiStub;

class DeleteCommandTest {

    @Test
    void testIsExit() {
        DeleteCommand deleteCommand = new DeleteCommand(0);
        assertFalse(deleteCommand.isExit(), "isExit should return false for DeleteCommand");
    }

    @Test
    void testGetName() {
        DeleteCommand deleteCommand = new DeleteCommand(0);
        assertEquals("delete", deleteCommand.getName(), "Command name should be 'delete'");
    }

    @Test
    void execute_validIndex_deletesTask() throws CodyException {
        TaskList tasks = new TaskList(new TaskStub("Task 1"), new TaskStub("Task 2"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        DeleteCommand deleteCommand = new DeleteCommand(0);
        String expectedResponse = "Removed task:\n[Stub] Task 1\n\nNow there is 1 task!";
        TaskList expectedTasks = new TaskList(new TaskStub("Task 2"));

        deleteCommand.execute(tasks, ui, storage);

        assertEquals(1, tasks.size(), "Task list size should be 1 after deleting a task");
        assertEquals(expectedTasks, tasks, "Remaining tasks should match expected tasks");
        assertEquals(new TaskStub("Task 2"), tasks.get(0), "Remaining task should be 'Task 2'");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should confirm the task is deleted");
        assertEquals(1, storage.getSaveCallCount(), "Storage save should be called once");
    }

    @Test
    void execute_invalidIndex_throwsException() {
        TaskList tasks = new TaskList(new TaskStub("Task 1"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        DeleteCommand deleteCommand = new DeleteCommand(1); // Invalid index
        assertThrows(UserInputException.class, () -> deleteCommand.execute(tasks, ui, storage),
                "Executing with an invalid index should throw UserInputException");
        assertEquals(1, tasks.size(), "Task list size should still be 1");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }

    @Test
    void execute_emptyTaskList_throwsException() {
        TaskList tasks = new TaskList();
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        DeleteCommand deleteCommand = new DeleteCommand(0); // Invalid index for empty list
        assertThrows(UserInputException.class, () -> deleteCommand.execute(tasks, ui, storage),
                "Executing on an empty task list should throw UserInputException");
        assertTrue(tasks.isEmpty(), "Task list should still be empty");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }
}
