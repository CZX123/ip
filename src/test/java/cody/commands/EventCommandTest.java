package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import cody.data.Event;

class EventCommandTest {

    @Test
    void testGetName() {
        LocalDateTime from = LocalDateTime.of(2023, 10, 15, 10, 0);
        LocalDateTime to = LocalDateTime.of(2023, 10, 15, 12, 0);
        EventCommand eventCommand = new EventCommand("Project meeting", from, to);
        assertEquals("deadline", eventCommand.getName(), "Command name should be 'deadline'");
    }

    @Test
    void testCreateTask() {
        LocalDateTime from = LocalDateTime.of(2023, 10, 15, 10, 0);
        LocalDateTime to = LocalDateTime.of(2023, 10, 15, 12, 0);
        EventCommand eventCommand = new EventCommand("Project meeting", from, to);
        assertEquals(new Event("Project meeting", from, to), eventCommand.createTask(),
                "Task should match the created Event");
    }
}
