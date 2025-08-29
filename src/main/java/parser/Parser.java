package parser;

import commands.DeleteCommand;
import commands.MarkCommand;
import commands.UnmarkCommand;
import commands.base.Command;
import commands.base.CommandName;
import commands.DeadlineCommand;
import commands.EventCommand;
import commands.ExitCommand;
import commands.ListCommand;
import commands.TodoCommand;
import exceptions.UserInputException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    public Command parse(String fullCommand) throws UserInputException {
        String[] nameDescSplit = fullCommand.split(" ", 2);
        CommandName commandName = getName(fullCommand);
        switch (commandName) {
        case BYE, EXIT:
            return new ExitCommand();
        case LIST:
            return new ListCommand();
        case MARK:
            return new MarkCommand(getIndex(fullCommand));
        case UNMARK:
            return new UnmarkCommand(getIndex(fullCommand));
        case DELETE:
            return new DeleteCommand(getIndex(fullCommand));
        case TODO:
            if (nameDescSplit.length < 2) {
                throw new UserInputException("The description of a todo cannot be empty!");
            }
            return new TodoCommand(nameDescSplit[1]);
        case DEADLINE:
            if (!fullCommand.matches("deadline .+ /by .+")) {
                throw new UserInputException("Deadlines should follow this format:\n"
                        + "deadline <description> /by YYYY-MM-DD HHmm");
            }
            String[] descDateSplit = nameDescSplit[1].split(" /by ", 2);
            LocalDateTime by;
            try {
                by = LocalDateTime.parse(descDateSplit[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            } catch (DateTimeParseException e) {
                throw new UserInputException("The due date should be in this format: YYYY-MM-DD HHmm");
            }
            return new DeadlineCommand(descDateSplit[0], by);
        case EVENT:
            if (!fullCommand.matches("event .+ /from .+ /to .+")) {
                throw new UserInputException("Events should follow this format:\n"
                        + "event <description> /from YYYY-MM-DD HHmm /to YYYY-MM-DD HHmm");
            }
            String[] descDatesSplit = nameDescSplit[1].split(" /from ", 2);
            String[] fromToSplit = descDatesSplit[1].split(" /to ", 2);
            LocalDateTime from, to;
            try {
                from = LocalDateTime.parse(fromToSplit[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                to = LocalDateTime.parse(fromToSplit[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            } catch (DateTimeParseException e) {
                throw new UserInputException("The dates should be in this format: YYYY-MM-DD HHmm");
            }
            return new EventCommand(descDatesSplit[0], from, to);
        default:
            throw new UserInputException("⚠\uFE0F Invalid command!");
        }
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
