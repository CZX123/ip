package cody.storage;

import cody.data.Deadline;
import cody.data.Event;
import cody.data.TaskList;
import cody.data.Todo;
import cody.exceptions.StorageOperationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageTest {
    private static final String TEST_FILEPATH = "storage-test/storage-test.txt";
    private static final String TEST_FILE_CONTENT = """
            T | 1 | "Set up desktop"
            D | 0 | "Submit Quiz 1" | 2025-09-01T23:59
            E | 0 | "Orientation" | 2025-09-01T09:00 | 2025-09-04T18:00
            """;
    private static final String CORRUPTED_FILE_CONTENT = """
            T | 1 | "Set up desktop"
            C | 0 | "Submit Quiz 1" | 2025-09-01T23:59
            E | 0 | "Orientation" | 2025-09-01T09:00 | 2025-09-04T18:00
            """;

    private final Path path;

    private final TaskList tasks;

    StorageTest() {
        path = Paths.get(TEST_FILEPATH);
        tasks = new TaskList(List.of(new Todo("Set up desktop"),
                new Deadline("Submit Quiz 1", LocalDateTime.of(2025, 9, 1, 23, 59)),
                new Event("Orientation", LocalDateTime.of(2025, 9, 1, 9, 0), LocalDateTime.of(2025, 9, 4, 18, 0))));
        tasks.get(0).markDone();
    }

    private void clearFileAndParent() throws IOException {
        Files.deleteIfExists(path);
        Files.deleteIfExists(path.getParent());
    }

    @Test
    public void load_noExistingFile_returnsEmptyTaskList() throws IOException, StorageOperationException {
        clearFileAndParent();
        assertEquals(new Storage(TEST_FILEPATH).load(), new TaskList());
        assertFalse(Files.exists(path)); // file shouldn't be created on load
    }

    @Test
    public void load_testFile_returnsCorrectTaskList() throws IOException, StorageOperationException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, TEST_FILE_CONTENT);
        Storage storage = new Storage(TEST_FILEPATH);
        assertEquals(storage.load(), tasks);
        clearFileAndParent();
    }

    @Test
    public void load_corruptedFile_throwsException() throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, CORRUPTED_FILE_CONTENT);
        Storage storage = new Storage(TEST_FILEPATH);
        assertThrows(StorageOperationException.class, storage::load);
        clearFileAndParent();
    }

    @Test
    public void testSave() throws IOException, StorageOperationException {
        Storage storage = new Storage(TEST_FILEPATH);
        storage.save(tasks);
        String[] actualLines = Files.readAllLines(path).toArray(new String[] {});
        String[] expectedLines = TEST_FILE_CONTENT.split("\n");
        assertEquals(actualLines.length, expectedLines.length);
        for (int i = 0; i < actualLines.length; i++) {
            assertEquals(actualLines[i], expectedLines[i]);
        }
        clearFileAndParent();
    }
}