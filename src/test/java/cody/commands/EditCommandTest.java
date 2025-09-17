package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import cody.data.Deadline;
import cody.data.Event;
import cody.data.TaskList;
import cody.data.Todo;
import cody.exceptions.CodyException;
import cody.exceptions.UserInputException;
import cody.testutil.StorageStub;
import cody.testutil.UiStub;

class EditCommandTest {

    @Test
    void testIsExit() {
        EditCommand editCommand = new EditCommand(0, List.of());
        assertFalse(editCommand.isExit(), "isExit should return false for EditCommand");
    }

    @Test
    void testGetName() {
        EditCommand editCommand = new EditCommand(0, List.of());
        assertEquals("edit", editCommand.getName(), "Command name should be 'edit'");
    }

    @Test
    void execute_validTodoEdit() throws CodyException {
        TaskList tasks = new TaskList(new Todo("Task 1"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        EditCommand editCommand = new EditCommand(0, List.of(new EditCommand.Option("desc", "Updated Task 1")));
        String expectedResponse = "Task edited!\n\nOriginal:\n[T][ ] Task 1\n\nUpdated:\n[T][ ] Updated Task 1";

        editCommand.execute(tasks, ui, storage);

        assertEquals(1, tasks.size(), "Task list size should remain the same after editing");
        assertEquals(new Todo("Updated Task 1"), tasks.get(0), "Task should be updated correctly");
        assertEquals(expectedResponse, ui.getCodyResponses(), "UI response should confirm the task is updated");
        assertEquals(1, storage.getSaveCallCount(), "Storage save should be called once");
    }

    @Test
    void execute_validDeadlineEdits() throws CodyException {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        TaskList tasks = new TaskList(new Deadline("Task 1", by));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        EditCommand editCommand1 = new EditCommand(0, List.of(new EditCommand.Option("desc", "Updated Task 1")));
        String expectedResponse1 = """
                Task edited!

                Original:
                [D][ ] Task 1 (by: 15 Oct 2023 2:00PM)

                Updated:
                [D][ ] Updated Task 1 (by: 15 Oct 2023 2:00PM)""";

        editCommand1.execute(tasks, ui, storage);

        assertEquals(1, tasks.size(), "Task list size should remain the same after editing");
        assertEquals(new Deadline("Updated Task 1", by), tasks.get(0), "Task should be updated correctly");
        assertEquals(expectedResponse1, ui.getCodyResponses(), "UI response should confirm the task is updated");
        assertEquals(1, storage.getSaveCallCount(), "Storage save should be called once");

        EditCommand editCommand2 = new EditCommand(0, List.of(new EditCommand.Option("by", "2023-10-20 1600")));
        String expectedResponse2 = """
                Task edited!

                Original:
                [D][ ] Updated Task 1 (by: 15 Oct 2023 2:00PM)

                Updated:
                [D][ ] Updated Task 1 (by: 20 Oct 2023 4:00PM)""";

        editCommand2.execute(tasks, ui, storage);

        assertEquals(1, tasks.size(), "Task list size should remain the same after editing");
        assertEquals(new Deadline("Updated Task 1", LocalDateTime.of(2023, 10, 20, 16, 0)), tasks.get(0),
                "Task should be updated correctly");
        assertEquals(expectedResponse1 + "\n" + expectedResponse2, ui.getCodyResponses(),
                "UI response should confirm the task is updated");
        assertEquals(2, storage.getSaveCallCount(), "Storage save should be called twice");
    }

    @Test
    void execute_validEventEdits() throws CodyException {
        LocalDateTime from = LocalDateTime.of(2023, 10, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2023, 10, 15, 16, 0);
        TaskList tasks = new TaskList(new Event("Task 1", from, to));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        EditCommand editCommand1 = new EditCommand(0, List.of(new EditCommand.Option("desc", "Updated Task 1")));
        String expectedResponse1 = """
                Task edited!

                Original:
                [E][ ] Task 1 (from: 15 Oct 2023 2:00PM to: 15 Oct 2023 4:00PM)

                Updated:
                [E][ ] Updated Task 1 (from: 15 Oct 2023 2:00PM to: 15 Oct 2023 4:00PM)""";

        editCommand1.execute(tasks, ui, storage);

        assertEquals(1, tasks.size(), "Task list size should remain the same after editing");
        assertEquals(new Event("Updated Task 1", from, to), tasks.get(0), "Task should be updated correctly");
        assertEquals(expectedResponse1, ui.getCodyResponses(), "UI response should confirm the task is updated");
        assertEquals(1, storage.getSaveCallCount(), "Storage save should be called once");

        EditCommand editCommand2 = new EditCommand(0,
                List.of(new EditCommand.Option("from", "2023-10-20 1600"),
                        new EditCommand.Option("to", "2023-10-20 1800")));
        String expectedResponse2 = """
                Task edited!

                Original:
                [E][ ] Updated Task 1 (from: 15 Oct 2023 2:00PM to: 15 Oct 2023 4:00PM)

                Updated:
                [E][ ] Updated Task 1 (from: 20 Oct 2023 4:00PM to: 20 Oct 2023 6:00PM)""";

        editCommand2.execute(tasks, ui, storage);

        assertEquals(1, tasks.size(), "Task list size should remain the same after editing");
        assertEquals(new Event("Updated Task 1", LocalDateTime.of(2023, 10, 20, 16, 0),
                LocalDateTime.of(2023, 10, 20, 18, 0)), tasks.get(0), "Task should be updated correctly");
        assertEquals(expectedResponse1 + "\n" + expectedResponse2, ui.getCodyResponses(),
                "UI response should confirm the task is updated");
        assertEquals(2, storage.getSaveCallCount(), "Storage save should be called twice");
    }

    @Test
    void execute_invalidDates_throwsException() {
        TaskList tasks = new TaskList(
                new Deadline("Task 1", LocalDateTime.of(2023, 10, 15, 14, 0)),
                new Event("Task 2", LocalDateTime.of(2023, 10, 15, 14, 0), LocalDateTime.of(2023, 10, 15, 16, 0))
        );
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        EditCommand editCommand1 = new EditCommand(0, List.of(new EditCommand.Option("by", "15-10-2023 1400")));
        assertThrows(UserInputException.class, () -> editCommand1.execute(tasks, ui, storage),
                "Executing with an invalid date format should throw UserInputException");

        EditCommand editCommand2 = new EditCommand(1, List.of(new EditCommand.Option("from", "15-10-2023 1400")));
        assertThrows(UserInputException.class, () -> editCommand2.execute(tasks, ui, storage),
                "Executing with an invalid date format should throw UserInputException");

        EditCommand editCommand3 = new EditCommand(1, List.of(new EditCommand.Option("to", "2023-10-15 1400")));
        assertThrows(UserInputException.class, () -> editCommand3.execute(tasks, ui, storage),
                "Executing with to date being equal to from date should throw UserInputException");

        EditCommand editCommand4 = new EditCommand(1, List.of(new EditCommand.Option("from", "2023-10-15 1800")));
        assertThrows(UserInputException.class, () -> editCommand4.execute(tasks, ui, storage),
                "Executing with to date being before to from date should throw UserInputException");

        assertEquals(2, tasks.size(), "Task list size should remain unchanged on any exception");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on any exception");
    }

    @Test
    void execute_invalidOption_throwsException() {
        TaskList tasks = new TaskList(new Todo("Task 1"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        EditCommand editCommand = new EditCommand(0, List.of(new EditCommand.Option("invalid", "value")));
        assertThrows(UserInputException.class, () -> editCommand.execute(tasks, ui, storage),
                "Executing with an invalid option should throw UserInputException");
        assertEquals(1, tasks.size(), "Task list size should remain unchanged on exception");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }

    @Test
    void execute_invalidIndex_throwsException() {
        TaskList tasks = new TaskList(new Todo("Task 1"));
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        EditCommand editCommand = new EditCommand(1, List.of(new EditCommand.Option("desc", "Updated Task 1")));
        assertThrows(UserInputException.class, () -> editCommand.execute(tasks, ui, storage),
                "Executing with an invalid index should throw UserInputException");
        assertEquals(1, tasks.size(), "Task list size should remain unchanged on exception");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }

    @Test
    void execute_emptyTaskList_throwsException() {
        TaskList tasks = new TaskList();
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        EditCommand editCommand = new EditCommand(0, List.of(new EditCommand.Option("desc", "Updated Task 1")));
        assertThrows(UserInputException.class, () -> editCommand.execute(tasks, ui, storage),
                "Executing on an empty task list should throw UserInputException");
        assertEquals(0, storage.getSaveCallCount(), "Storage save should not be called on exception");
    }

    @Test
    void testEquals() {
        EditCommand editCommand1 = new EditCommand(0, List.of(new EditCommand.Option("desc", "Task 1")));
        EditCommand editCommand2 = new EditCommand(0, List.of(new EditCommand.Option("desc", "Task 1")));
        EditCommand editCommand3 = new EditCommand(1, List.of(new EditCommand.Option("desc", "Task 1")));
        EditCommand editCommand4 = new EditCommand(0, List.of(new EditCommand.Option("desc", "Task 2")));

        assertEquals(editCommand1, editCommand2, "Commands with the same index and options should be equal");
        assertNotEquals(editCommand1, editCommand3, "Commands with different indices should not be equal");
        assertNotEquals(editCommand1, editCommand4, "Commands with different options should not be equal");
    }

    @Test
    void testHashCode() {
        EditCommand editCommand1 = new EditCommand(0, List.of(new EditCommand.Option("desc", "Task 1")));
        EditCommand editCommand2 = new EditCommand(0, List.of(new EditCommand.Option("desc", "Task 1")));

        assertEquals(editCommand1.hashCode(), editCommand2.hashCode(),
                "Hash codes should match for commands with the same index and options");
    }
}
