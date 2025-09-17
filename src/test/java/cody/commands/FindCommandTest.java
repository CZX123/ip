package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import cody.data.TaskList;
import cody.testutil.StorageStub;
import cody.testutil.TaskStub;
import cody.testutil.UiStub;

class FindCommandTest {

    @Test
    void testIsExit() {
        FindCommand findCommand = new FindCommand("keyword");
        assertFalse(findCommand.isExit(), "isExit should return false for FindCommand");
    }

    @Test
    void testGetName() {
        FindCommand findCommand = new FindCommand("keyword");
        assertEquals("find", findCommand.getName(), "Command name should be 'find'");
    }

    @Test
    void execute_noMatchingTasks() {
        TaskList tasks = new TaskList(new TaskStub("Task 1"), new TaskStub("Task 2"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        FindCommand findCommand = new FindCommand("Nonexistent");
        String expectedResponse = "There are no tasks that match \"Nonexistent\"";

        findCommand.execute(tasks, ui, storage);
        assertEquals(2, tasks.size(), "Task list size should still be 2");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should indicate no matching tasks");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on find command");
    }

    @Test
    void execute_singleMatchingTask() {
        TaskList tasks = new TaskList(new TaskStub("Task 1"), new TaskStub("Task 2"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        FindCommand findCommand = new FindCommand("Task 1");
        String expectedResponse = """
                There is 1 matching task:
                [Stub] Task 1""";

        findCommand.execute(tasks, ui, storage);
        assertEquals(2, tasks.size(), "Task list size should still be 2");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should list the single matching task");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on find command");
    }

    @Test
    void execute_multipleMatchingTasks() {
        TaskList tasks = new TaskList(
                new TaskStub("Task 1"),
                new TaskStub("Task 2"),
                new TaskStub("Task 1 Meeting")
        );
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        FindCommand findCommand = new FindCommand("Task 1");
        String expectedResponse = """
                There are 2 matching tasks:
                [Stub] Task 1
                [Stub] Task 1 Meeting""";

        findCommand.execute(tasks, ui, storage);
        assertEquals(3, tasks.size(), "Task list size should still be 3");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should list all matching tasks");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on find command");
    }

    @Test
    void testEquals() {
        FindCommand findCommand1 = new FindCommand("keyword");
        FindCommand findCommand2 = new FindCommand("keyword");
        FindCommand findCommand3 = new FindCommand("different");

        assertEquals(findCommand1, findCommand2, "Commands with the same keyword should be equal");
        assertNotEquals(findCommand1, findCommand3, "Commands with different keywords should not be equal");
    }

    @Test
    void testHashCode() {
        FindCommand findCommand1 = new FindCommand("keyword");
        FindCommand findCommand2 = new FindCommand("keyword");

        assertEquals(findCommand1.hashCode(), findCommand2.hashCode(),
                "Hash codes should match for commands with the same keyword");
    }
}
