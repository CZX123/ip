package cody.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import cody.data.Todo;

class TodoCommandTest {

    @Test
    void testGetName() {
        TodoCommand todoCommand = new TodoCommand("Sample task");
        assertEquals("todo", todoCommand.getName(), "Command name should be 'todo'");
    }

    @Test
    void testCreateTask() {
        TodoCommand todoCommand = new TodoCommand("Sample task");
        assertEquals(new Todo("Sample task"), todoCommand.createTask(), "Task should match the created Todo");
    }
}
