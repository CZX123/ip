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

    private static Storage instance;

    private Storage() {}

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
            String line = String.format("%s | %d | \"%s\"", letter, task.isDone() ? 1 : 0, task.getDescription());
            switch (letter) {
            case 'T':
                break;
            case 'D':
                Deadline deadline = (Deadline) task;
                line += String.format(" | %s", deadline.getBy());
                break;
            case 'E':
                Event event = (Event) task;
                line += String.format(" | %s | %s", event.getFrom(), event.getTo());
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
            int firstQuotePosition = line.indexOf('\"');
            int lastQuotePosition = line.lastIndexOf('\"');
            boolean isDone = line.charAt(4) != '0';
            String description = line.substring(firstQuotePosition + 1, lastQuotePosition);
            Task task;
            switch (line.charAt(0)) {
            case 'T':
                task = new Todo(description);
                break;
            case 'D':
                String byText = line.substring(lastQuotePosition).split(" \\| ", 2)[1];
                LocalDateTime by;
                try {
                    by = LocalDateTime.parse(byText);
                } catch (DateTimeParseException e) {
                    throw new TaskDecodeException("Unable to parse date from this line:\n" + line);
                }
                task = new Deadline(description, by);
                break;
            case 'E':
                String[] split = line.substring(lastQuotePosition).split(" \\| ", 3);
                LocalDateTime from;
                LocalDateTime to;
                try {
                    from = LocalDateTime.parse(split[1]);
                    to = LocalDateTime.parse(split[2]);
                } catch (DateTimeParseException e) {
                    throw new TaskDecodeException("Unable to parse date from this line:\n" + line);
                }
                task = new Event(description, from, to);
                break;
            default:
                throw new TaskDecodeException("Invalid task type: " + line.charAt(0));
            }
            if (isDone) {
                task.markDone();
            }
            tasks.add(task);
        }
        return new TaskList(tasks);
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
     * @param tasks the task list to save
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
