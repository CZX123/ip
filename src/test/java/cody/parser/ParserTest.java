package cody.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import cody.commands.DeadlineCommand;
import cody.commands.DeleteCommand;
import cody.commands.EventCommand;
import cody.commands.ExitCommand;
import cody.commands.ListCommand;
import cody.commands.MarkCommand;
import cody.commands.TodoCommand;
import cody.commands.UnmarkCommand;
import cody.commands.base.Command;
import cody.exceptions.UserInputException;

public class ParserTest {
    private final String[] validInputs = {
            "bye",
            "exit",
            "list",
            "list 2025-09-01",
            "mark 1",
            "unmark 2",
            "delete 3",
            "delete 999",
            "todo Set up desktop",
            "deadline Submit Quiz 1 /by 2025-09-01 2359",
            "event Orientation /from 2025-09-01 0900 /to 2025-09-04 1800"};
    private final Command[] outputs = {
            new ExitCommand(),
            new ExitCommand(),
            new ListCommand(),
            new ListCommand(LocalDate.of(2025, 9, 1)),
            new MarkCommand(0),
            new UnmarkCommand(1),
            new DeleteCommand(2),
            new DeleteCommand(998),
            new TodoCommand("Set up desktop"),
            new DeadlineCommand("Submit Quiz 1", LocalDateTime.of(2025, 9, 1, 23, 59)),
            new EventCommand("Orientation", LocalDateTime.of(2025, 9, 1, 9, 0),
                    LocalDateTime.of(2025, 9, 4, 18, 0))};

    private final String[] invalidInputs = {
            "create", "task", "help", // invalid command
            "list 22 June", // wrong date format
            "mark", "unmark", "delete", // missing task id
            "mark Task 5", "unmark Event 2", "delete Deadline 4", // invalid task id
            "todo", "deadline", "event", // missing description or other details
            "deadline D", // missing due date
            "deadline D 2025-09-01 2359", // invalid format
            "deadline D /by 2025/09/01 11:59pm", // wrong date format
            "deadline D /by 2025-09-01 2800", // invalid time
            "event E", // missing from and to dates
            "event E 2025-09-01 0900 to 2025-09-04 1800", // invalid format
            "event E /from 2025/09/01 9:00am /to 2025/09/01 6:00pm", // wrong date format
            "event E /from 2025-09-01 0900 /to 2025-09-32 1800" // invalid day of month
    };

    @Test
    public void parse_validInputs_returnsCorrectCommands() throws UserInputException {
        for (int i = 0; i < validInputs.length; i++) {
            assertEquals(Parser.parse(validInputs[i]), outputs[i]);
        }
    }

    @Test
    public void parse_invalidInputs_throwsException() {
        for (String invalidInput : invalidInputs) {
            assertThrows(UserInputException.class, () -> Parser.parse(invalidInput));
        }
    }
}
