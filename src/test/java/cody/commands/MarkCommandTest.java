package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import cody.data.Task;
import cody.data.TaskList;
import cody.data.Todo;
import cody.exceptions.CodyException;
import cody.exceptions.UserInputException;
import cody.testutil.StorageStub;
import cody.testutil.UiStub;

class MarkCommandTest {

    @Test
    void testGetName() {
        MarkCommand markCommand = new MarkCommand(0);
        assertEquals("mark", markCommand.getName(), "Command name should be 'mark'");
    }

    @Test
    void testGetIndex() {
        int index = 2;
        MarkCommand markCommand = new MarkCommand(index);
        assertEquals(index, markCommand.getIndex(), "Index should match the provided value");
    }

    @Test
    void execute_validIndex_marksTaskAsDone() throws CodyException {
        TaskList tasks = new TaskList(List.of(new Todo("Task 1")));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        MarkCommand markCommand = new MarkCommand(0);
        String expectedResponses = "Marked task as done:\n[T][X] Task 1";
        Task expectedTask = new Todo("Task 1");
        expectedTask.markDone();

        markCommand.execute(tasks, ui, storage);
        assertEquals(expectedTask, tasks.get(0), "Task should be marked as done");
        assertEquals(expectedResponses, ui.getCodyResponses(), "UI response should confirm the task is marked as done");
        assertEquals(1, storage.getSaveCallCount(), "Storage save should be called once");
    }

    @Test
    void execute_invalidIndex_throwsException() {
        TaskList tasks = new TaskList(List.of(new Todo("Task 1")));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        MarkCommand markCommand = new MarkCommand(1); // Invalid index
        assertThrows(UserInputException.class, () -> markCommand.execute(tasks, ui, storage),
                "Executing with an invalid index should throw UserInputException");
        assertEquals(1, tasks.size(), "Task list size should still be 1");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }

    @Test
    void execute_emptyTaskList_throwsException() {
        TaskList tasks = new TaskList();
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        MarkCommand markCommand = new MarkCommand(0); // Invalid index for empty list
        assertThrows(UserInputException.class, () -> markCommand.execute(tasks, ui, storage),
                "Executing on an empty task list should throw UserInputException");
        assertTrue(tasks.isEmpty(), "Task list should still be empty");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }
}
