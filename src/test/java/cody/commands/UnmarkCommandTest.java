package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cody.data.Task;
import cody.data.TaskList;
import cody.data.Todo;
import cody.exceptions.CodyException;
import cody.exceptions.UserInputException;
import cody.testutil.StorageStub;
import cody.testutil.UiStub;

class UnmarkCommandTest {

    @Test
    void testGetName() {
        UnmarkCommand unmarkCommand = new UnmarkCommand(0);
        assertEquals("unmark", unmarkCommand.getName(), "Command name should be 'unmark'");
    }

    @Test
    void testGetIndex() {
        int index = 2;
        UnmarkCommand unmarkCommand = new UnmarkCommand(index);
        assertEquals(index, unmarkCommand.getIndex(), "Command index should match the provided index");
    }

    @Test
    void execute_validIndex_marksTaskAsNotDone() throws CodyException {
        TaskList tasks = new TaskList(new Todo("Task 1"));
        tasks.get(0).markDone(); // Mark the task as done initially
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        UnmarkCommand unmarkCommand = new UnmarkCommand(0);
        String expectedResponses = "Marked task as not done:\n[T][ ] Task 1";
        Task expectedTask = new Todo("Task 1");

        unmarkCommand.execute(tasks, ui, storage);
        assertEquals(expectedTask, tasks.get(0), "Task should be marked as not done");
        assertEquals(expectedResponses, ui.getCodyResponses(),
                "UI response should confirm the task is marked as not done");
        assertEquals(1, storage.getSaveCallCount(), "Storage save should be called once");
    }

    @Test
    void execute_invalidIndex_throwsException() {
        TaskList tasks = new TaskList(new Todo("Task 1"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        UnmarkCommand unmarkCommand = new UnmarkCommand(1); // Invalid index
        assertThrows(UserInputException.class, () -> unmarkCommand.execute(tasks, ui, storage),
                "Executing with an invalid index should throw UserInputException");
        assertEquals(1, tasks.size(), "Task list size should still be 1");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }

    @Test
    void execute_emptyTaskList_throwsException() {
        TaskList tasks = new TaskList();
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        UnmarkCommand unmarkCommand = new UnmarkCommand(0); // Invalid index for empty list
        assertThrows(UserInputException.class, () -> unmarkCommand.execute(tasks, ui, storage),
                "Executing on an empty task list should throw UserInputException");
        assertTrue(tasks.isEmpty(), "Task list should still be empty");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }
}
