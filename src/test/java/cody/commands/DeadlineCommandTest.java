package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import cody.data.Deadline;

class DeadlineCommandTest {

    @Test
    void testGetName() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        DeadlineCommand deadlineCommand = new DeadlineCommand("Submit assignment", by);
        assertEquals("deadline", deadlineCommand.getName(), "Command name should be 'deadline'");
    }

    @Test
    void testCreateTask() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        DeadlineCommand deadlineCommand = new DeadlineCommand("Submit assignment", by);
        assertEquals(new Deadline("Submit assignment", by), deadlineCommand.createTask(),
                "Task should match the created Deadline");
    }
}
