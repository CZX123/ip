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
    private static final Path DEFAULT_FILEPATH = Paths.get(Storage.DEFAULT_FILEPATH);
    private static final String TEST_FILEPATH_TEXT = "storage-test/storage-test.txt";
    private static final Path TEST_FILEPATH = Paths.get(TEST_FILEPATH_TEXT);
    private static final String TEST_FILE_CONTENT = """
            T | 1 | "Set up desktop"
            D | 0 | "Submit Quiz 1" | 2025-09-01T23:59
            E | 0 | "Orientation" | 2025-09-01T09:00 | 2025-09-04T18:00
            """;
    private static final String CORRUPTED_FILE_CONTENT = """
            T | 1 | "Set up desktop"
            D | 0 | "Submit Quiz 1" | "2025-09-01T23:59"
            E | 0 | "Orientation" | 2025-09-01T09:00 | 2025-09-04T18:00
            """;

    private static List<String> initialFileContents;
    private static TaskList tasks;

    @BeforeAll
    public static void setup() {
        tasks = new TaskList(List.of(new Todo("Set up desktop"),
                new Deadline("Submit Quiz 1", LocalDateTime.of(2025, 9, 1, 23, 59)),
                new Event("Orientation", LocalDateTime.of(2025, 9, 1, 9, 0), LocalDateTime.of(2025, 9, 4, 18, 0))));
        tasks.get(0).markDone();
        // Gets the initial data file contents, so we can reset it after all tests are finished.
        try {
            initialFileContents = Files.readAllLines(DEFAULT_FILEPATH);
        } catch (IOException e) {
            initialFileContents = null;
        }
    }

    @AfterAll
    public static void cleanup() {
        // Resets the initial data file back to original.
        if (initialFileContents != null) {
            try {
                Path path = Paths.get(Storage.DEFAULT_FILEPATH);
                Files.createDirectories(path.getParent());
                Files.write(path, initialFileContents);
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
        Files.deleteIfExists(DEFAULT_FILEPATH);
        Files.deleteIfExists(DEFAULT_FILEPATH.getParent());
        Files.deleteIfExists(TEST_FILEPATH);
        Files.deleteIfExists(TEST_FILEPATH.getParent());
    }

    @Test
    public void load_noExistingFile_returnsEmptyTaskList() throws IOException, StorageOperationException {
        resetFilePaths();
        assertEquals(Storage.getInstance().load(), new TaskList());
        assertEquals(Storage.getInstance().load(TEST_FILEPATH_TEXT), new TaskList());
        assertFalse(Files.exists(DEFAULT_FILEPATH)); // file shouldn't be created on load
        assertFalse(Files.exists(TEST_FILEPATH)); // file shouldn't be created on load
    }

    @Test
    public void load_testFile_returnsCorrectTaskList() throws IOException, StorageOperationException {
        Files.createDirectories(DEFAULT_FILEPATH.getParent());
        Files.writeString(DEFAULT_FILEPATH, TEST_FILE_CONTENT);
        Files.createDirectories(TEST_FILEPATH.getParent());
        Files.writeString(TEST_FILEPATH, TEST_FILE_CONTENT);
        assertEquals(Storage.getInstance().load(), tasks);
        assertEquals(Storage.getInstance().load(TEST_FILEPATH_TEXT), tasks);
        resetFilePaths();
    }

    @Test
    public void load_corruptedFile_throwsException() throws IOException {
        Files.createDirectories(DEFAULT_FILEPATH.getParent());
        Files.writeString(DEFAULT_FILEPATH, CORRUPTED_FILE_CONTENT);
        Files.createDirectories(TEST_FILEPATH.getParent());
        Files.writeString(TEST_FILEPATH, CORRUPTED_FILE_CONTENT);
        assertThrows(StorageOperationException.class, () -> Storage.getInstance().load());
        assertThrows(StorageOperationException.class, () -> Storage.getInstance().load(TEST_FILEPATH_TEXT));
        resetFilePaths();
    }

    @Test
    public void testSave() throws IOException, StorageOperationException {
        Storage.getInstance().save(tasks);
        Storage.getInstance().save(tasks, TEST_FILEPATH_TEXT);
        String[] actualLinesDefaultFilePath = Files.readAllLines(TEST_FILEPATH).toArray(new String[] {});
        String[] actualLinesTestFilePath = Files.readAllLines(TEST_FILEPATH).toArray(new String[] {});
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
