package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import cody.data.TaskList;
import cody.testutil.StorageStub;
import cody.testutil.TaskStub;
import cody.testutil.UiStub;

class ListCommandTest {

    @Test
    void testIsExit() {
        ListCommand listCommand = new ListCommand();
        assertFalse(listCommand.isExit(), "isExit should return false for ListCommand");
    }

    @Test
    void testGetName() {
        ListCommand listCommand = new ListCommand();
        assertEquals("list", listCommand.getName(), "Command name should be 'list'");
    }

    @Test
    void execute_emptyTaskList() {
        TaskList tasks = new TaskList();
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        ListCommand listCommand = new ListCommand();
        String expectedResponse = "You have no tasks saved!";

        listCommand.execute(tasks, ui, storage);
        assertEquals(0, tasks.size(), "Task list size should still be 0");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should indicate no tasks are saved");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on list command");
    }

    @Test
    void execute_singleItemTaskList() {
        TaskList tasks = new TaskList(new TaskStub("Task 1"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        ListCommand listCommand = new ListCommand();
        String expectedResponses = """
                You have 1 task!
                1. [Stub] Task 1""";

        listCommand.execute(tasks, ui, storage);
        assertEquals(1, tasks.size(), "Task list size should still be 1");
        assertEquals(expectedResponses, ui.getCodyResponses(), "UI response should list all tasks");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on list command");
    }

    @Test
    void execute_multipleItemsTaskList() {
        TaskList tasks = new TaskList(new TaskStub("Task 1"), new TaskStub("Task 2"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        ListCommand listCommand = new ListCommand();
        String expectedResponses = """
                You have 2 tasks!
                1. [Stub] Task 1
                2. [Stub] Task 2""";

        listCommand.execute(tasks, ui, storage);
        assertEquals(2, tasks.size(), "Task list size should still be 2");
        assertEquals(expectedResponses, ui.getCodyResponses(), "UI response should list all tasks");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on list command");
    }

    @Test
    void execute_filteredTaskList_noTasksOnDate() {
        TaskList tasks = new TaskList(new TaskStub("Task 1"), new TaskStub("Task 2"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        LocalDate filterDate = LocalDate.of(2023, 10, 16);
        ListCommand listCommand = new ListCommand(filterDate);
        String expectedResponse = "You have no tasks on 16 Oct 2023!";

        listCommand.execute(tasks, ui, storage);
        assertEquals(2, tasks.size(), "Task list size should still be 2");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should indicate no tasks on the given date");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on list command");
    }

    @Test
    void execute_filteredTaskList_singleTaskOnDate() {
        TaskList tasks = new TaskList(new TaskStub("Task 1"), new TaskStub("Task 2", true), new TaskStub("Task 3"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        LocalDate filterDate = LocalDate.of(2023, 10, 15);
        ListCommand listCommand = new ListCommand(filterDate);
        String expectedResponse = """
                You have 1 task on 15 Oct 2023!
                [Stub] Task 2""";
        listCommand.execute(tasks, ui, storage);

        assertEquals(3, tasks.size(), "Task list size should still be 3");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should list the task on the given date");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on list command");
    }

    @Test
    void execute_filteredTaskList_multipleTasksOnDate() {
        TaskList tasks = new TaskList(new TaskStub("Task 1"), new TaskStub("Task 2", true),
                new TaskStub("Task 3", true));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        LocalDate filterDate = LocalDate.of(2023, 10, 15);
        ListCommand listCommand = new ListCommand(filterDate);
        String expectedResponse = """
                You have 2 tasks on 15 Oct 2023!
                [Stub] Task 2
                [Stub] Task 3""";
        listCommand.execute(tasks, ui, storage);

        assertEquals(3, tasks.size(), "Task list size should still be 3");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should list tasks on the given date");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on list command");
    }

    @Test
    void testEquals() {
        LocalDate date = LocalDate.of(2023, 10, 15);
        ListCommand listCommand1 = new ListCommand(date);
        ListCommand listCommand2 = new ListCommand(date);
        ListCommand listCommand3 = new ListCommand(LocalDate.of(2023, 10, 16));
        ListCommand listCommand4 = new ListCommand();
        ListCommand listCommand5 = new ListCommand();

        assertEquals(listCommand1, listCommand2, "Commands with the same date should be equal");
        assertNotEquals(listCommand1, listCommand3, "Commands with different dates should not be equal");
        assertNotEquals(listCommand1, listCommand4, "Commands with and without dates should not be equal");
        assertEquals(listCommand4, listCommand5, "Commands without dates should be equal");
    }

    @Test
    void testHashCode() {
        LocalDate date = LocalDate.of(2023, 10, 15);

        ListCommand listCommand1 = new ListCommand(date);
        ListCommand listCommand2 = new ListCommand(date);
        ListCommand listCommand3 = new ListCommand();
        ListCommand listCommand4 = new ListCommand();

        assertEquals(listCommand1.hashCode(), listCommand2.hashCode(),
                "Hash codes should match for commands with the same date");
        assertEquals(listCommand3.hashCode(), listCommand4.hashCode(),
                "Hash codes should match for commands without dates");
    }
}
