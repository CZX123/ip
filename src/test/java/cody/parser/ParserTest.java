package cody.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import org.junit.jupiter.params.provider.MethodSource;

import cody.commands.DeadlineCommand;
import cody.commands.DeleteCommand;
import cody.commands.EditCommand;
import cody.commands.EventCommand;
import cody.commands.ExitCommand;
import cody.commands.ListCommand;
import cody.commands.MarkCommand;
import cody.commands.TodoCommand;
import cody.commands.UnmarkCommand;
import cody.commands.base.Command;
import cody.exceptions.UserInputException;

public class ParserTest {
    private static final String[] VALID_INPUTS = {
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
            "event Orientation /from 2025-09-01 0900 /to 2025-09-04 1800",
            "edit 1 /desc new description",
            "edit 999 /option value"};
    private static final Command[] VALID_OUTPUTS = {
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
                    LocalDateTime.of(2025, 9, 4, 18, 0)),
            new EditCommand(0, List.of(new EditCommand.Option("desc", "new description"))),
            new EditCommand(998, List.of(new EditCommand.Option("option", "value")))};

    // CHECKSTYLE OFF: SingleSpaceSeparator
    // Comments are aligned for better readability
    @SuppressWarnings("unused") // INVALID_INPUTS is used by @FieldSource
    private static final String[] INVALID_INPUTS = {
            "create", "task", "help",                               // invalid commands
            "list 22 June",                                         // wrong date format
            "mark", "unmark", "delete", "edit",                     // missing task id
            "mark Task 5", "unmark e2", "delete four", "edit todo", // invalid task id
            "todo", "deadline", "event",                            // missing description or other details
            "deadline D",                                           // missing due date
            "deadline D 2025-09-01 2359",                           // invalid format
            "deadline D /by 2025/09/01 11:59pm",                    // wrong date format
            "deadline D /by 2025-09-01 2800",                       // invalid time
            "event E",                                              // missing from and to dates
            "event E 2025-09-01 0900 to 2025-09-04 1800",           // invalid format
            "event E /from 2025/09/01 9:00 /to 2025/09/01 6:00",    // wrong date format
            "event E /from 2025-09-01 0900 /to 2025-09-32 1800",    // invalid day of month
            "event E /from 2025-09-01 0900 /to 2025-09-01 2500",    // invalid time
            "event E /from 2025-09-01 1800 /to 2025-09-01 0900",    // from date is after to date
            "event E /from 2025-09-01 1800 /to 2025-09-01 1800",    // from date is equal to date
            "edit 1 new description",                               // invalid edit format 1
            "edit 2 new description /by 2025-09-01 0900"            // invalid edit format 2
    };
    // CHECKSTYLE ON: SingleSpaceSeparator

    private static Stream<Arguments> provideValidInputsAndOutputs() {
        return IntStream.range(0, VALID_INPUTS.length)
                .mapToObj(i -> Arguments.of(VALID_INPUTS[i], VALID_OUTPUTS[i]));
    }

    @ParameterizedTest
    @MethodSource("provideValidInputsAndOutputs")
    public void parse_validInput_returnsCorrectCommand(String input, Command output) throws UserInputException {
        assertEquals(output, Parser.parse(input));
    }

    @ParameterizedTest
    @FieldSource("INVALID_INPUTS")
    public void parse_invalidInput_throwsException(String input) {
        assertThrows(UserInputException.class, () -> Parser.parse(input));
    }

    @Test
    public void parseDateTimeFromString_validInput_returnsCorrectDateTime() throws DateTimeParseException {
        String input = "2025-09-01 2359";
        LocalDateTime expected = LocalDateTime.of(2025, 9, 1, 23, 59);
        assertEquals(expected, Parser.parseDateTimeFromString(input));
    }

    @Test
    public void parseDateTimeFromString_inValidInput_throwsException() {
        String input = "2025/09/01 23:59";
        assertThrows(DateTimeParseException.class, () -> Parser.parseDateTimeFromString(input));
    }
}
