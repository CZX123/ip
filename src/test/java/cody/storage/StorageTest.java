package cody.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cody.data.Deadline;
import cody.data.Event;
import cody.data.TaskList;
import cody.data.Todo;
import cody.exceptions.StorageOperationException;

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

    private static List<String> initialFileContents;
    private final Path defaultFilePath;
    private final Path testFilePath;
    private final TaskList tasks;

    StorageTest() {
        testFilePath = Paths.get(TEST_FILEPATH);
        defaultFilePath = Paths.get(Storage.DEFAULT_FILEPATH);
        tasks = new TaskList(List.of(new Todo("Set up desktop"),
                new Deadline("Submit Quiz 1", LocalDateTime.of(2025, 9, 1, 23, 59)),
                new Event("Orientation", LocalDateTime.of(2025, 9, 1, 9, 0), LocalDateTime.of(2025, 9, 4, 18, 0))));
        tasks.get(0).markDone();
    }

    @BeforeAll
    public static void setup() {
        // Gets the initial data file contents, so we can reset it after all tests are finished.
        try {
            initialFileContents = Files.readAllLines(Paths.get(Storage.DEFAULT_FILEPATH));
        } catch (IOException e) {
            initialFileContents = null;
        }
    }

    @AfterAll
    public static void cleanup() {
        // Resets the initial data file back to original.
        if (initialFileContents != null) {
            try {
                Files.write(Paths.get(Storage.DEFAULT_FILEPATH), initialFileContents);
            } catch (IOException e) {
                System.out.println("Error saving original data file! Dumping file contents here:");
                for (String line : initialFileContents) {
                    System.out.println(line);
                }
                throw new RuntimeException(e);
            }
        }
    }

    private void resetFilePaths() throws IOException {
        Files.deleteIfExists(defaultFilePath);
        Files.deleteIfExists(defaultFilePath.getParent());
        Files.deleteIfExists(testFilePath);
        Files.deleteIfExists(testFilePath.getParent());
    }

    @Test
    public void load_noExistingFile_returnsEmptyTaskList() throws IOException, StorageOperationException {
        resetFilePaths();
        assertEquals(Storage.getInstance().load(), new TaskList());
        assertEquals(Storage.getInstance().load(TEST_FILEPATH), new TaskList());
        assertFalse(Files.exists(defaultFilePath)); // file shouldn't be created on load
        assertFalse(Files.exists(testFilePath)); // file shouldn't be created on load
    }

    @Test
    public void load_testFile_returnsCorrectTaskList() throws IOException, StorageOperationException {
        Files.createDirectories(defaultFilePath.getParent());
        Files.writeString(defaultFilePath, TEST_FILE_CONTENT);
        Files.createDirectories(testFilePath.getParent());
        Files.writeString(testFilePath, TEST_FILE_CONTENT);
        assertEquals(Storage.getInstance().load(), tasks);
        assertEquals(Storage.getInstance().load(TEST_FILEPATH), tasks);
        resetFilePaths();
    }

    @Test
    public void load_corruptedFile_throwsException() throws IOException {
        Files.createDirectories(defaultFilePath.getParent());
        Files.writeString(defaultFilePath, CORRUPTED_FILE_CONTENT);
        Files.createDirectories(testFilePath.getParent());
        Files.writeString(testFilePath, CORRUPTED_FILE_CONTENT);
        assertThrows(StorageOperationException.class, () -> Storage.getInstance().load());
        assertThrows(StorageOperationException.class, () -> Storage.getInstance().load(TEST_FILEPATH));
        resetFilePaths();
    }

    @Test
    public void testSave() throws IOException, StorageOperationException {
        Storage.getInstance().save(tasks);
        Storage.getInstance().save(tasks, TEST_FILEPATH);
        String[] actualLinesDefaultFilePath = Files.readAllLines(testFilePath).toArray(new String[] {});
        String[] actualLinesTestFilePath = Files.readAllLines(testFilePath).toArray(new String[] {});
        String[] expectedLines = TEST_FILE_CONTENT.split("\n");
        assertEquals(actualLinesDefaultFilePath.length, expectedLines.length);
        assertEquals(actualLinesTestFilePath.length, expectedLines.length);
        for (int i = 0; i < expectedLines.length; i++) {
            assertEquals(actualLinesDefaultFilePath[i], expectedLines[i]);
            assertEquals(actualLinesTestFilePath[i], expectedLines[i]);
        }
        resetFilePaths();
    }
}
