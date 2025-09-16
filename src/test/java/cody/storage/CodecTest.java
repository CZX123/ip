package cody.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;

import cody.data.Deadline;
import cody.data.Event;
import cody.data.Task;
import cody.data.TaskList;
import cody.data.Todo;

public class CodecTest {
    private static final String TEST_FILE_CONTENT = """
            T | 0 | "test"
            D | 1 | "test" | 2025-12-09T18:00
            E | 1 | "test" | 2025-12-09T18:00 | 2025-12-10T18:00
            T | 1 | "test "test" | test |"
            D | 0 | ""test" | test | test" | 2100-01-01T00:00
            E | 1 | "test | test | "test"" | 2000-01-01T00:00 | 2100-01-01T00:00
            T | 0 | "test" | "test" | "test"
            """;

    // CHECKSTYLE OFF: SingleSpaceSeparator
    // Comments are aligned for better readability
    @SuppressWarnings("unused") // INVALID_INPUTS is used by @FieldSource
    private static final String[] INVALID_INPUTS = {
            // general formatting
            "Text that does not conform to the format",                               // random text
            "T, 0, \"test\"",                                                         // wrong separator
            "T|0|\"test\"",                                                           // wrong spacing 1
            "T  |  0  |  \"test\"",                                                   // wrong spacing 2
            // task letter
            "F | 0 | \"test\"",                                                       // invalid task letter 1
            "0 | 1 | \"test\"",                                                       // invalid task letter 2
            // task status
            "T | X | \"test\"",                                                       // invalid task status 1
            "T | 2 | \"test\"",                                                       // invalid task status 2
            // todos
            "T | 0",                                                                  // missing description
            "T | 1 | test",                                                           // missing quotes
            "T | 0 | \"test\" | 2025-12-09T18:00",                                    // extra field
            // deadlines
            "D | 0",                                                                  // missing description
            "D | 1 | test",                                                           // missing quotes 1
            "D | 0 | 2025-12-09T18:00",                                               // missing quotes 2
            "D | 1 | \"test\"",                                                       // missing date
            "D | 0 | \"test\" | invalid-date",                                        // invalid date 1
            "D | 1 | \"test\" | \"date\"",                                            // invalid date 2 (with quotes)
            "D | 0 | \"test\" | 2025-12-09 | 18:00",                                  // invalid date 3 (with separator)
            "D | 1 | \"test\" | 2025-12-09",                                          // missing time
            "D | 0 | \"test\" | 2025/12/09 18:00",                                    // wrong date format
            "D | 0 | \"test\" | 2025-12-09T18:00 | \"test\"",                         // extra field 1
            "D | 0 | \"test\" | 2025-12-09T18:00 | 2025-12-10T18:00",                 // extra field 2
            // events
            "E | 0",                                                                  // missing description
            "E | 1 | test",                                                           // missing quotes 1
            "E | 0 | 2025-12-09T18:00",                                               // missing quotes 2
            "E | 1 | \"test\"",                                                       // missing dates
            "E | 0 | \"test\" | 2025-12-09T18:00",                                    // missing to date
            "E | 1 | \"test\" | invalid-date | 2025-12-10T18:00",                     // invalid from date
            "E | 0 | \"test\" | 2025-12-09T18:00 | invalid-date",                     // invalid to date
            "E | 1 | \"test\" | invalid-date | invalid-date",                         // invalid dates
            "E | 0 | \"test\" | \"date\" | 2025-12-10T18:00",                         // invalid date (with quotes)
            "E | 1 | \"test\" | 2025-12-09T18:00 | \"date\"",                         // invalid date (with quotes)
            "E | 0 | \"test\" | 2025-12-09 | 18:00",                                  // invalid date (with separator)
            "E | 1 | \"test\" | 2025-12-09 | 18:00 | 2025-12-09 | 18:00",             // invalid date (with separator)
            "E | 0 | \"test\" | 2025-12-09 | 2025-12-10",                             // missing times
            "E | 1 | \"test\" | 2025/12/09 18:00 | 2025/12/10 18:00",                 // wrong date format
            "E | 0 | \"test\" | 2025-12-09T18:00 | 2025-12-10T18:00 | \"test\"",      // extra field 1
            "E | 1 | \"t\" | 2025-12-09T18:00 | 2025-12-10T18:00 | 2025-12-11T18:00", // extra field 2
            "E | 0 | \"test\" | 2025-12-10T18:00 | 2025-12-09T18:00",                 // to date before from date
            "E | 1 | \"test\" | 2025-12-09T18:01 | 2025-12-09T18:00"                  // to date equal to from date
    };
    // CHECKSTYLE ON: SingleSpaceSeparator

    private static Codec codec;
    private static TaskList tasks;

    @BeforeAll
    public static void setup() {
        codec = new Codec();
        Task[] taskArray = {
                new Todo("test"),
                new Deadline("test", LocalDateTime.of(2025, 12, 9, 18, 0)),
                new Event("test", LocalDateTime.of(2025, 12, 9, 18, 0),
                        LocalDateTime.of(2025, 12, 10, 18, 0)),
                // tasks with special characters in description
                new Todo("test \"test\" | test |"),
                new Deadline("\"test\" | test | test", LocalDateTime.of(2100, 1, 1, 0, 0)),
                new Event("test | test | \"test\"", LocalDateTime.of(2000, 1, 1, 0, 0),
                        LocalDateTime.of(2100, 1, 1, 0, 0)),
                new Todo("test\" | \"test\" | \"test")};
        taskArray[1].markDone();
        taskArray[2].markDone();
        taskArray[3].markDone();
        taskArray[5].markDone();
        tasks = new TaskList(List.of(taskArray));
    }

    @Test
    public void encode_validTaskList_returnsCorrectTextFormat() throws Codec.TaskEncodeException {
        List<String> expected = List.of(TEST_FILE_CONTENT.split("\n"));
        List<String> actual = codec.encode(tasks);
        assertEquals(7, actual.size());
        for (int i = 0; i < 7; i++) {
            assertEquals(expected.get(i), actual.get(i));
        };
    }

    @Test
    public void decode_validInputs_returnsCorrectTaskList() throws Codec.TaskDecodeException {
        TaskList expected = tasks;
        TaskList actual = codec.decode(List.of(TEST_FILE_CONTENT.split("\n")));
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @FieldSource("INVALID_INPUTS")
    public void decode_invalidInput_throwsException(String input) {
        assertThrows(Codec.TaskDecodeException.class, () -> codec.decode(List.of(input)));
    }
}
