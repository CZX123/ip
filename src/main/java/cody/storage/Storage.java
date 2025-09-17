package cody.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import cody.data.TaskList;
import cody.exceptions.StorageOperationException;

/**
 * Handles saving and loading from storage.
 */
public class Storage {
    public static final String DEFAULT_FILEPATH = "data/tasks.txt";

    private final Codec codec = new Codec();

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
            return codec.decode(Files.readAllLines(path));
        } catch (Codec.TaskDecodeException e) {
            throw new StorageOperationException("Error loading file:\n\n" + e.getMessage()
                    + "\n\nNote that adding any tasks now will overwrite the save file!"
                    + "\nYou can recover your saved tasks at " + filePath);
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
            Files.write(path, codec.encode(tasks));
        } catch (Codec.TaskEncodeException e) {
            throw new StorageOperationException("Error writing to file:\n\n" + e.getMessage());
        } catch (IOException e) {
            throw new StorageOperationException("Error writing to file: " + path);
        }
    }
}
