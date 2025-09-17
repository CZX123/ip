package cody.testutil;

import cody.data.TaskList;
import cody.storage.Storage;

/**
 * A stub of {@code Storage} for testing purposes.
 * Does not perform any actual file operations.
 */
public class StorageStub extends Storage {

    /**
     * Does nothing in this stub.
     */
    @Override
    public void save(TaskList tasks) {
        // do nothing
    }

    /**
     * Does nothing in this stub.
     */
    @Override
    public void save(TaskList tasks, String filePath) {
        // do nothing
    }

    /**
     * Returns an empty TaskList in this stub.
     */
    @Override
    public TaskList load() {
        return new TaskList();
    }

    /**
     * Returns an empty TaskList in this stub.
     */
    @Override
    public TaskList load(String filePath) {
        return new TaskList();
    }
}
