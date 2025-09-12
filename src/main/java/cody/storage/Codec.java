package cody.storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import cody.data.Deadline;
import cody.data.Event;
import cody.data.Task;
import cody.data.TaskList;
import cody.data.Todo;
import cody.exceptions.StorageOperationException;

/**
 * Handles encoding and decoding of task list from lines of text.
 */
class Codec {
    private static final String SEPARATOR = " | ";
    private static final char QUOTE = '\"';
    private static final char STATUS_DONE = '1';
    private static final char STATUS_NOT_DONE = '0';

    /** Regex used for matching the separator. */
    private static final String REGEX_SEPARATOR = " \\| ";

    /**
     * Regex used for checking task format.
     * Checks that the task letter, status digit and description follow the correct format.
     */
    private static final String REGEX_TASK = "[" + Todo.LETTER + "|" + Deadline.LETTER + "|" + Event.LETTER + "]"
            + REGEX_SEPARATOR + "[" + STATUS_DONE + "|" + STATUS_NOT_DONE + "]"
            + REGEX_SEPARATOR + QUOTE + ".+" + QUOTE;

    /**
     * Regex used for checking date format.
     * <p>
     * It does not check whether date can be parsed into a {@code LocalDateTime},
     * but only checks there are no special characters like {@code QUOTE} or
     * {@code SEPARATOR} that may corrupt the data and cause bugs during decoding.
     */
    private static final String REGEX_DATE = "(?:(?!" + QUOTE + "|" + REGEX_SEPARATOR + ").)*";

    /**
     * Encodes the given task list into lines of text used for storage.
     *
     * @param tasks the task list to encode
     * @return lines of text representing the task list
     * @throws TaskEncodeException when a task cannot be encoded
     */
    public List<String> encode(TaskList tasks) throws TaskEncodeException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            String line = String.valueOf(task.getLetter());
            line += SEPARATOR + (task.isDone() ? STATUS_DONE : STATUS_NOT_DONE);
            line += SEPARATOR + QUOTE + task.getDescription() + QUOTE;

            switch (task.getLetter()) {
            case Todo.LETTER:
                break;
            case Deadline.LETTER:
                Deadline deadline = (Deadline) task;
                line += SEPARATOR + deadline.getBy();
                break;
            case Event.LETTER:
                Event event = (Event) task;
                line += SEPARATOR + event.getFrom();
                line += SEPARATOR + event.getTo();
                break;
            default:
                throw new TaskEncodeException("Unable to encode this task: " + task);
            }

            lines.add(line);
        }
        return lines;
    }

    /**
     * Decodes lines of text representing the task list into a {@code TaskList} object.
     *
     * @param lines lines of text representing the task list
     * @return a {@code TaskList} object containing all tasks from the lines of text
     * @throws TaskDecodeException when a line cannot be decoded due to invalid format
     */
    public TaskList decode(List<String> lines) throws TaskDecodeException {
        List<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            // CHECKSTYLE OFF: Indentation
            // switch expression can have indentation
            Task task = switch (line.charAt(0)) {
                case Todo.LETTER -> decodeTodo(line);
                case Deadline.LETTER -> decodeDeadline(line);
                case Event.LETTER -> decodeEvent(line);
                default -> throw new TaskDecodeException("Invalid task type: " + line.charAt(0));
            };
            // CHECKSTYLE ON: Indentation
            boolean isDone = line.charAt(4) == STATUS_DONE; // status is at index 4
            if (isDone) {
                task.markDone();
            }
            tasks.add(task);
        }
        return new TaskList(tasks);
    }

    private Todo decodeTodo(String line) throws TaskDecodeException {
        if (!matchesTodoFormat(line)) {
            throw new TaskDecodeException("Invalid todo format:\n" + line);
        }
        return new Todo(getDescription(line));
    }

    private Deadline decodeDeadline(String line) throws TaskDecodeException {
        if (!matchesDeadlineFormat(line)) {
            throw new TaskDecodeException("Invalid deadline format:\n" + line);
        }
        int byPosition = line.lastIndexOf(QUOTE) + SEPARATOR.length() + 1;
        String byText = line.substring(byPosition);
        LocalDateTime by;
        try {
            by = LocalDateTime.parse(byText);
        } catch (DateTimeParseException e) {
            throw new TaskDecodeException("Unable to parse date from this line:\n" + line);
        }
        return new Deadline(getDescription(line), by);
    }

    private Event decodeEvent(String line) throws TaskDecodeException {
        if (!matchesEventFormat(line)) {
            throw new TaskDecodeException("Invalid event format:\n" + line);
        }
        // splits into 3 items: ", <from-date>, <to-date>
        String[] lineSplit = line.substring(line.lastIndexOf(QUOTE)).split(REGEX_SEPARATOR, 3);
        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(lineSplit[1]);
            to = LocalDateTime.parse(lineSplit[2]);
        } catch (DateTimeParseException e) {
            throw new TaskDecodeException("Unable to parse date from this line:\n" + line);
        }
        return new Event(getDescription(line), from, to);
    }

    private boolean matchesTodoFormat(String line) {
        return line.charAt(0) == Todo.LETTER && line.matches(REGEX_TASK);
    }

    private boolean matchesDeadlineFormat(String line) {
        return line.charAt(0) == Deadline.LETTER
                && line.matches(REGEX_TASK + REGEX_SEPARATOR + REGEX_DATE);
    }

    private boolean matchesEventFormat(String line) {
        return line.charAt(0) == Event.LETTER
                && line.matches(REGEX_TASK + REGEX_SEPARATOR + REGEX_DATE + REGEX_SEPARATOR + REGEX_DATE);
    }

    private String getDescription(String line) {
        int firstQuotePosition = line.indexOf(QUOTE);
        int lastQuotePosition = line.lastIndexOf(QUOTE);
        return line.substring(firstQuotePosition + 1, lastQuotePosition);
    }

    public static class TaskEncodeException extends StorageOperationException {
        public TaskEncodeException(String message) {
            super(message);
        }
    }

    public static class TaskDecodeException extends StorageOperationException {
        public TaskDecodeException(String message) {
            super(message);
        }
    }
}
