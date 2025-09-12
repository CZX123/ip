package cody.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import cody.commands.base.CommandName;
import cody.commands.base.ModifyTaskCommand;
import cody.data.Deadline;
import cody.data.Event;
import cody.data.Task;
import cody.data.TaskList;
import cody.data.Todo;
import cody.exceptions.CodyException;
import cody.exceptions.UserInputException;
import cody.parser.Parser;
import cody.storage.Storage;
import cody.ui.Ui;

/**
 * Edits a task description and dates based on the given options.
 */
public class EditCommand extends ModifyTaskCommand {
    private static final String OPTION_DESC = "desc";
    private static final String OPTION_BY = "by";
    private static final String OPTION_FROM = "from";
    private static final String OPTION_TO = "to";
    private static final Map<Character, List<String>> VALID_OPTIONS = Map.of(
            Todo.LETTER, List.of(OPTION_DESC),
            Deadline.LETTER, List.of(OPTION_DESC, OPTION_BY),
            Event.LETTER, List.of(OPTION_DESC, OPTION_FROM, OPTION_TO)
    );

    /**
     * Represents an option as part of an edit command.
     *
     * @param name  option name, written in text as "/option-name"
     * @param value option value, written after option name
     */
    public record Option(String name, String value) {
    }

    private final List<Option> options;

    /**
     * Constructs an edit command.
     *
     * @param index   the index of the task to be edited
     * @param options the new values for the task to be updated to
     */
    public EditCommand(int index, List<Option> options) {
        super(CommandName.EDIT.getName(), index);
        this.options = options;
    }

    @Override
    public void execute(TaskList tasks) throws CodyException {
        Task originalTask = tasks.get(getIndex());
        checkValidity(originalTask);

        // CHECKSTYLE OFF: Indentation
        // switch expression can have indentation
        Task newTask = switch (originalTask.getLetter()) {
            case Todo.LETTER -> updateTodo();
            case Deadline.LETTER -> updateDeadline((Deadline) originalTask);
            case Event.LETTER -> updateEvent((Event) originalTask);
            default -> throw new RuntimeException();
        };
        // CHECKSTYLE ON: Indentation

        if (originalTask.isDone()) {
            newTask.markDone();
        }

        tasks.remove(getIndex());
        tasks.add(getIndex(), newTask);

        Ui.getInstance().showCodyResponse("Task edited!\n"
                + "\nOriginal:\n" + originalTask
                + "\n\nUpdated:\n" + newTask);
        Storage.getInstance().save(tasks);
    }

    private Todo updateTodo() {
        assert options.size() == 1;
        assert options.get(0).name().equals(VALID_OPTIONS.get(Todo.LETTER).get(0)); // Option should only be "/desc"
        return new Todo(options.get(0).value());
    }

    private Deadline updateDeadline(Deadline original) throws UserInputException {
        String description = original.getDescription();
        LocalDateTime by = original.getBy();

        for (Option option : options) {
            if (option.name().equals(OPTION_DESC)) {
                description = option.value();
                continue;
            }

            assert option.name().equals(OPTION_BY);
            try {
                by = Parser.parseDateTimeFromString(option.value());
            } catch (DateTimeParseException e) {
                throw new UserInputException("The due date should be in this format: YYYY-MM-DD HHmm");
            }
        }

        return new Deadline(description, by);
    }

    private Event updateEvent(Event original) throws UserInputException {
        String description = original.getDescription();
        LocalDateTime from = original.getFrom();
        LocalDateTime to = original.getTo();

        for (Option option : options) {
            if (option.name().equals(OPTION_DESC)) {
                description = option.value();
                continue;
            }

            assert option.name().equals(OPTION_FROM) || option.name().equals(OPTION_TO);
            LocalDateTime dateTime;
            try {
                dateTime = Parser.parseDateTimeFromString(option.value());
            } catch (DateTimeParseException e) {
                throw new UserInputException("The dates should be in this format: YYYY-MM-DD HHmm");
            }
            if (option.name().equals(OPTION_FROM)) {
                from = dateTime;
            } else {
                to = dateTime;
            }
        }

        if (from.isAfter(to)) {
            throw new UserInputException("Invalid dates provided!\n"
                    + "The starting date and time occurs after the ending date and time!");
        }

        return new Event(description, from, to);
    }

    /**
     * Checks that the provided options are valid.
     *
     * @param task the task to match the options to
     * @throws UserInputException if there is an invalid option
     */
    private void checkValidity(Task task) throws UserInputException {
        List<String> validOptionNames = VALID_OPTIONS.get(task.getLetter());
        for (Option option : options) {
            if (!validOptionNames.contains(option.name())) {
                String taskType = task.getClass().getSimpleName().toLowerCase();
                String validNames = validOptionNames.stream().reduce((a, b) -> a + ", " + b).orElse("");
                String errorMessage = "Invalid option name: " + option.name()
                        + "\nValid option names for " + taskType + ":\n" + validNames;
                throw new UserInputException(errorMessage);
            }
        }
    }
}
