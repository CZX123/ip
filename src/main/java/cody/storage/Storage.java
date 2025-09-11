package cody.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * Handles saving and loading from storage.
 */
public class Storage {
    public static final String DEFAULT_FILEPATH = "data/tasks.txt";

    private static final String SEPARATOR = " | ";
    private static final String SEPARATOR_REGEX = " \\| ";
    private static final char DESCRIPTION_QUOTE = '\"';
    private static final char STATUS_DONE = '1';
    private static final char STATUS_NOT_DONE = '0';

    private static Storage instance;

    private Storage() {
    }

    /**
     * Gets the currently active {@code Storage} instance.
     */
    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    /**
     * Encodes the given task list into lines of text used for storage.
     *
     * @param tasks the task list to encode
     * @return lines of text representing the task list
     * @throws TaskEncodeException when a task cannot be encoded
     */
    private List<String> encode(TaskList tasks) throws TaskEncodeException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            char letter = task.getLetter();
            char status = task.isDone() ? STATUS_DONE : STATUS_NOT_DONE;
            String description = task.getDescription();
            String line = letter + SEPARATOR
                    + status + SEPARATOR
                    + DESCRIPTION_QUOTE + description + DESCRIPTION_QUOTE;
            switch (letter) {
            case 'T':
                assert task instanceof Todo;
                break;
            case 'D':
                assert task instanceof Deadline;
                Deadline deadline = (Deadline) task;
                line += SEPARATOR + deadline.getBy();
                break;
            case 'E':
                assert task instanceof Event;
                Event event = (Event) task;
                line += SEPARATOR + event.getFrom() + SEPARATOR + event.getTo();
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
    private TaskList decode(List<String> lines) throws TaskDecodeException {
        List<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            if (!matchesTaskFormat(line)) {
                throw new TaskDecodeException("Invalid task:\n" + line);
            }

            int firstQuotePosition = line.indexOf(DESCRIPTION_QUOTE);
            int lastQuotePosition = line.lastIndexOf(DESCRIPTION_QUOTE);
            boolean isDone = line.charAt(4) == STATUS_DONE; // status is at index 4
            String description = line.substring(firstQuotePosition + 1, lastQuotePosition);
            // CHECKSTYLE OFF: Indentation
            // switch expression can have indentation
            Task task = switch (line.charAt(0)) {
                case 'T' -> decodeTodo(line, description);
                case 'D' -> decodeDeadline(line, description, lastQuotePosition);
                case 'E' -> decodeEvent(line, description, lastQuotePosition);
                default -> throw new TaskDecodeException("Invalid task type: " + line.charAt(0));
            };
            // CHECKSTYLE ON: Indentation
            if (isDone) {
                task.markDone();
            }
            tasks.add(task);
        }
        return new TaskList(tasks);
    }

    /**
     * Returns whether the given line matches the correct encoding format.
     */
    private boolean matchesTaskFormat(String line) {
        return line.matches("[A-Z]"
                + SEPARATOR_REGEX + "[" + STATUS_DONE + "|" + STATUS_NOT_DONE + "]"
                + SEPARATOR_REGEX + ".+");
    }

    private Todo decodeTodo(String line, String description) {
        assert line.charAt(0) == 'T';
        return new Todo(description);
    }

    private Deadline decodeDeadline(String line, String description, int lastQuotePosition) throws TaskDecodeException {
        assert line.charAt(0) == 'D';
        boolean isCorrectFormat = line.substring(lastQuotePosition).matches(DESCRIPTION_QUOTE + SEPARATOR_REGEX + ".+");
        if (!isCorrectFormat) {
            throw new TaskDecodeException("Invalid deadline format:\n" + line);
        }

        int byPosition = lastQuotePosition + 4;
        String byText = line.substring(byPosition);
        LocalDateTime by;
        try {
            by = LocalDateTime.parse(byText);
        } catch (DateTimeParseException e) {
            throw new TaskDecodeException("Unable to parse date from this line:\n" + line);
        }
        return new Deadline(description, by);
    }

    private Event decodeEvent(String line, String description, int lastQuotePosition) throws TaskDecodeException {
        assert line.charAt(0) == 'E';
        boolean isCorrectFormat = line.substring(lastQuotePosition)
                .matches(DESCRIPTION_QUOTE + SEPARATOR_REGEX + ".+" + SEPARATOR_REGEX + ".+");
        if (!isCorrectFormat) {
            throw new TaskDecodeException("Invalid event format:\n" + line);
        }

        String[] lineSplit = line.substring(lastQuotePosition).split(" \\| ", 3);
        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(lineSplit[1]);
            to = LocalDateTime.parse(lineSplit[2]);
        } catch (DateTimeParseException e) {
            throw new TaskDecodeException("Unable to parse date from this line:\n" + line);
        }
        return new Event(description, from, to);
    }

    /**
     * Loads task list from storage, located at default file path.
     *
     * @return task list
     * @throws StorageOperationException when an IO or decoding error occurs
     */
    public TaskList load() throws StorageOperationException {
        return load(DEFAULT_FILEPATH);
    }

    /**
     * Loads task list from storage, located at given file path.
     *
     * @param filePath path of the file
     * @return task list
     * @throws StorageOperationException when an IO or decoding error occurs
     */
    public TaskList load(String filePath) throws StorageOperationException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return new TaskList();
        }
        try {
            return decode(Files.readAllLines(path));
        } catch (IOException e) {
            throw new StorageOperationException("Error loading file: " + path);
        }
    }

    /**
     * Saves task list into storage, located at default file path.
     *
     * @param tasks the task list to save
     * @throws StorageOperationException when an IO or encoding error occurs
     */
    public void save(TaskList tasks) throws StorageOperationException {
        save(tasks, DEFAULT_FILEPATH);
    }

    /**
     * Saves task list into storage, located at given file path.
     *
     * @param tasks    the task list to save
     * @param filePath the path of the file
     * @throws StorageOperationException when an IO or encoding error occurs
     */
    public void save(TaskList tasks, String filePath) throws StorageOperationException {
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, encode(tasks));
        } catch (IOException e) {
            throw new StorageOperationException("Error writing to file: " + path);
        }
    }

    private static class TaskEncodeException extends StorageOperationException {
        public TaskEncodeException(String message) {
            super(message);
        }
    }

    private static class TaskDecodeException extends StorageOperationException {
        public TaskDecodeException(String message) {
            super(message);
        }
    }
}
