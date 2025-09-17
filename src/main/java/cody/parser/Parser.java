package cody.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cody.commands.DeadlineCommand;
import cody.commands.DeleteCommand;
import cody.commands.EditCommand;
import cody.commands.EventCommand;
import cody.commands.ExitCommand;
import cody.commands.FindCommand;
import cody.commands.ListCommand;
import cody.commands.MarkCommand;
import cody.commands.TodoCommand;
import cody.commands.UnmarkCommand;
import cody.commands.base.Command;
import cody.commands.base.CommandName;
import cody.exceptions.UserInputException;

/**
 * Parses user input.
 */
public class Parser {
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HHmm";

    private Parser() {}

    /**
     * Parses user input, and returns the corresponding command.
     *
     * @throws UserInputException when there is invalid input
     */
    public static Command parse(String fullCommand) throws UserInputException {
        // CHECKSTYLE OFF: Indentation
        // switch expression can have indentation, checkstyle does not differ between switch expressions and statements
        return switch (getName(fullCommand)) {
            case BYE, EXIT -> getExitCommand();
            case LIST -> parseListCommand(fullCommand);
            case FIND -> parseFindCommand(fullCommand);
            case TODO -> parseTodoCommand(fullCommand);
            case DEADLINE -> parseDeadlineCommand(fullCommand);
            case EVENT -> parseEventCommand(fullCommand);
            case MARK -> parseMarkCommand(fullCommand);
            case UNMARK -> parseUnmarkCommand(fullCommand);
            case DELETE -> parseDeleteCommand(fullCommand);
            case EDIT, UPDATE -> parseEditCommand(fullCommand);
        };
        // CHECKSTYLE ON: Indentation
    }

    private static Command getExitCommand() {
        return new ExitCommand();
    }

    private static Command parseListCommand(String fullCommand) throws UserInputException {
        if (fullCommand.trim().equals(CommandName.LIST.getName())) {
            return new ListCommand();
        } else {
            LocalDate date;
            try {
                date = LocalDate.parse(fullCommand.split(" ", 2)[1],
                        DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN));
            } catch (DateTimeParseException e) {
                throw new UserInputException("The date filter should be in this format: YYYY-MM-DD");
            }
            return new ListCommand(date);
        }
    }

    private static Command parseFindCommand(String fullCommand) throws UserInputException {
        String[] nameKeywordSplit = fullCommand.split(" ", 2);
        if (nameKeywordSplit.length == 1) {
            throw new UserInputException("Please enter search keyword!");
        }
        return new FindCommand(nameKeywordSplit[1]);
    }

    private static Command parseTodoCommand(String fullCommand) throws UserInputException {
        String[] nameDescSplit = fullCommand.split(" ", 2);
        if (nameDescSplit.length < 2) {
            throw new UserInputException("The description of a todo cannot be empty!");
        }
        return new TodoCommand(nameDescSplit[1]);
    }

    private static Command parseDeadlineCommand(String fullCommand) throws UserInputException {
        String[] nameOthersSplit = fullCommand.split(" ", 2);
        if (!fullCommand.matches("deadline .+ /by .+")) {
            throw new UserInputException("Deadlines should follow this format:\n"
                    + "deadline <description> /by YYYY-MM-DD HHmm");
        }
        String[] descDateSplit = nameOthersSplit[1].split(" /by ", 2);
        LocalDateTime by;
        try {
            by = parseDateTimeFromString(descDateSplit[1]);
        } catch (DateTimeParseException e) {
            throw new UserInputException("The due date should be in this format: YYYY-MM-DD HHmm");
        }
        return new DeadlineCommand(descDateSplit[0], by);
    }

    private static Command parseEventCommand(String fullCommand) throws UserInputException {
        String[] nameOthersSplit = fullCommand.split(" ", 2);
        if (!fullCommand.matches("event .+ /from .+ /to .+")) {
            throw new UserInputException("Events should follow this format:\n"
                    + "event <description> /from YYYY-MM-DD HHmm /to YYYY-MM-DD HHmm");
        }
        String[] descDatesSplit = nameOthersSplit[1].split(" /from ", 2);
        String[] fromToSplit = descDatesSplit[1].split(" /to ", 2);
        LocalDateTime from;
        LocalDateTime to;
        try {
            from = parseDateTimeFromString(fromToSplit[0]);
            to = parseDateTimeFromString(fromToSplit[1]);
        } catch (DateTimeParseException e) {
            throw new UserInputException("The dates should be in this format: YYYY-MM-DD HHmm");
        }
        if (!from.isBefore(to)) {
            throw new UserInputException("Event start date should be before event end date!");
        }
        return new EventCommand(descDatesSplit[0], from, to);
    }

    private static Command parseMarkCommand(String fullCommand) throws UserInputException {
        return new MarkCommand(getIndex(fullCommand));
    }

    private static Command parseUnmarkCommand(String fullCommand) throws UserInputException {
        return new UnmarkCommand(getIndex(fullCommand));
    }

    private static Command parseDeleteCommand(String fullCommand) throws UserInputException {
        return new DeleteCommand(getIndex(fullCommand));
    }

    private static Command parseEditCommand(String fullCommand) throws UserInputException {
        return new EditCommand(getIndex(fullCommand), parseEditOptions(fullCommand));
    }

    private static List<EditCommand.Option> parseEditOptions(String fullCommand) throws UserInputException {
        String[] commandIndexOptionsSplit = fullCommand.split(" ", 3);
        if (commandIndexOptionsSplit.length < 3) {
            throw new UserInputException("Missing edit options!\n"
                    + "Edit options should follow this format: /option-name <new value>");
        }
        List<EditCommand.Option> options = new ArrayList<>();
        String optionsText = commandIndexOptionsSplit[2];
        if (!optionsText.startsWith("/")) {
            throw new UserInputException("Invalid edit option format!\n"
                    + "Edit options should follow this format: /option-name <new value>");
        }
        for (String optionText : optionsText.split("/")) {
            if (optionText.isEmpty()) {
                continue;
            }
            String[] nameValueSplit = optionText.split(" ", 2);
            String name = nameValueSplit[0];
            if (nameValueSplit.length < 2) {
                throw new UserInputException("Missing value after /" + name);
            }
            String value = nameValueSplit[1].trim();
            options.add(new EditCommand.Option(nameValueSplit[0], value));
        }
        return options;
    }

    /**
     * Gets the command name from user input.
     *
     * @param fullCommand user input
     * @return the corresponding {@code CommandName} enum
     * @throws UserInputException if command is invalid
     */
    private static CommandName getName(String fullCommand) throws UserInputException {
        String firstWord = fullCommand.split(" ", 2)[0];
        return Arrays.stream(CommandName.values())
                .filter(commandName -> firstWord.equals(commandName.getName()))
                .findFirst()
                .orElseThrow(() -> new UserInputException("Invalid command!"));
    }

    /**
     * Gets task index from user input, for commands which include keying in
     * task id.
     *
     * @param fullCommand user input
     * @return index of the task in the task list
     * @throws UserInputException if the task id given is invalid
     */
    private static int getIndex(String fullCommand) throws UserInputException {
        int index;
        try {
            index = Integer.parseInt(fullCommand.split(" ", 3)[1]) - 1;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new UserInputException("Please enter a valid task number!\n"
                    + "To view task number, type \"list\".");
        }
        if (index < 0) {
            throw new UserInputException(String.format("There is no task numbered %d!", index + 1));
        }
        return index;
    }

    /**
     * Parses the given string into a date-time object.
     *
     * @param text the date and time written as a string, with the format following the default date-time pattern
     * @return a {@code LocalDateTime} instance representing the given date and time
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalDateTime parseDateTimeFromString(String text) throws DateTimeParseException {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
    }
}
