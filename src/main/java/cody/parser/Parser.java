package cody.parser;

import cody.commands.DeleteCommand;
import cody.commands.FindCommand;
import cody.commands.MarkCommand;
import cody.commands.UnmarkCommand;
import cody.commands.base.Command;
import cody.commands.base.CommandName;
import cody.commands.DeadlineCommand;
import cody.commands.EventCommand;
import cody.commands.ExitCommand;
import cody.commands.ListCommand;
import cody.commands.TodoCommand;
import cody.exceptions.UserInputException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    public Command parse(String fullCommand) throws UserInputException {
        String[] nameDescSplit = fullCommand.split(" ", 2);
        CommandName commandName = getName(fullCommand);
        return switch (commandName) {
            case BYE, EXIT -> new ExitCommand();
            case MARK -> new MarkCommand(getIndex(fullCommand));
            case UNMARK -> new UnmarkCommand(getIndex(fullCommand));
            case DELETE -> new DeleteCommand(getIndex(fullCommand));
            case LIST -> parseListCommand(fullCommand);
            case FIND -> parseFindCommand(fullCommand);
            case TODO -> parseTodoCommand(fullCommand);
            case DEADLINE -> parseDeadlineCommand(fullCommand);
            case EVENT -> parseEventCommand(fullCommand);
        };
    }

    private Command parseListCommand(String fullCommand) throws UserInputException {
        if (fullCommand.trim().equals(CommandName.LIST.getName())) {
            return new ListCommand();
        } else {
            LocalDate date;
            try {
                date = LocalDate.parse(fullCommand.split(" ", 2)[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                throw new UserInputException("The date filter should be in this format: YYYY-MM-DD");
            }
            return new ListCommand(date);
        }
    }

    private Command parseFindCommand(String fullCommand) throws UserInputException {
        String[] nameKeywordSplit = fullCommand.split(" ", 2);
        if (nameKeywordSplit.length == 1) {
            throw new UserInputException("Please enter search keyword!");
        }
        return new FindCommand(nameKeywordSplit[1]);
    }

    private Command parseTodoCommand(String fullCommand) throws UserInputException {
        String[] nameDescSplit = fullCommand.split(" ", 2);
        if (nameDescSplit.length < 2) {
            throw new UserInputException("The description of a todo cannot be empty!");
        }
        return new TodoCommand(nameDescSplit[1]);
    }

    private Command parseDeadlineCommand(String fullCommand) throws UserInputException {
        String[] nameOthersSplit = fullCommand.split(" ", 2);
        if (!fullCommand.matches("deadline .+ /by .+")) {
            throw new UserInputException("Deadlines should follow this format:\n"
                    + "deadline <description> /by YYYY-MM-DD HHmm");
        }
        String[] descDateSplit = nameOthersSplit[1].split(" /by ", 2);
        LocalDateTime by;
        try {
            by = LocalDateTime.parse(descDateSplit[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new UserInputException("The due date should be in this format: YYYY-MM-DD HHmm");
        }
        return new DeadlineCommand(descDateSplit[0], by);
    }

    private Command parseEventCommand(String fullCommand) throws UserInputException {
        String[] nameOthersSplit = fullCommand.split(" ", 2);
        if (!fullCommand.matches("event .+ /from .+ /to .+")) {
            throw new UserInputException("Events should follow this format:\n"
                    + "event <description> /from YYYY-MM-DD HHmm /to YYYY-MM-DD HHmm");
        }
        String[] descDatesSplit = nameOthersSplit[1].split(" /from ", 2);
        String[] fromToSplit = descDatesSplit[1].split(" /to ", 2);
        LocalDateTime from, to;
        try {
            from = LocalDateTime.parse(fromToSplit[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            to = LocalDateTime.parse(fromToSplit[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new UserInputException("The dates should be in this format: YYYY-MM-DD HHmm");
        }
        return new EventCommand(descDatesSplit[0], from, to);
    }

    private static CommandName getName(String fullCommand) throws UserInputException {
        String firstWord = fullCommand.split(" ", 2)[0];
        for (CommandName commandName : CommandName.values()) {
            if (firstWord.equals(commandName.getName())) {
                return commandName;
            }
        }
        throw new UserInputException("⚠\uFE0F Invalid command!");
    }

    private int getIndex(String fullCommand) throws UserInputException {
        int index;
        try {
            index = Integer.parseInt(fullCommand.split(" ", 2)[1]) - 1;
        } catch (Exception e) {
            throw new UserInputException("Please enter a valid task number! \uD83E\uDD74\n"
                    + "To view task number, type \"list\".");
        }
        if (index < 0) {
            throw new UserInputException(String.format("There is no task numbered %d! \uD83D\uDE35", index + 1));
        }
        return index;
    }
}
