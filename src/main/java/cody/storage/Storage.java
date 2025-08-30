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

public class Storage {
    private static final String DEFAULT_FILEPATH = "data/tasks.txt";

    private final Path path;

    public Storage() {
        this(DEFAULT_FILEPATH);
    }

    public Storage(String filePath) {
        path = Paths.get(filePath);
    }

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

    public TaskList load() throws StorageOperationException {
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return new TaskList();
        }
        try {
            return decode(Files.readAllLines(path));
        } catch (IOException e) {
            throw new StorageOperationException("Error loading file: " + path);
        }
    }

    public void save(TaskList tasks) throws StorageOperationException {
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
