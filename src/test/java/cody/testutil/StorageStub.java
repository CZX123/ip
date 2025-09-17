package cody.testutil;

import cody.data.TaskList;
import cody.storage.Storage;

/**
 * A stub of {@code Storage} for testing purposes.
 * Does not perform any actual file operations, but counts the number of save calls.
 */
public class StorageStub extends Storage {
    private int saveCallCount = 0;
    private int loadCallCount = 0;

    /**
     * Returns the number of times the save method has been called.
     */
    public int getSaveCallCount() {
        return saveCallCount;
    }

    /**
     * Returns the number of times the load method has been called.
     */
    public int getLoadCallCount() {
        return loadCallCount;
    }

    /**
     * Increments the save call count. Does not perform any file operations.
     */
    @Override
    public void save(TaskList tasks) {
        saveCallCount++;
    }

    /**
     * Increments the save call count. Does not perform any file operations.
     */
    @Override
    public void save(TaskList tasks, String filePath) {
        saveCallCount++;
    }

    /**
     * Returns an empty TaskList.
     */
    @Override
    public TaskList load() {
        loadCallCount++;
        return new TaskList();
    }

    /**
     * Returns an empty TaskList.
     */
    @Override
    public TaskList load(String filePath) {
        loadCallCount++;
        return new TaskList();
    }
}
