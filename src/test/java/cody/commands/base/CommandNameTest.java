package cody.commands.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CommandNameTest {

    @Test
    void testGetName() {
        assertEquals("bye", CommandName.BYE.getName(), "Command name should be 'bye'");
        assertEquals("exit", CommandName.EXIT.getName(), "Command name should be 'exit'");
        assertEquals("list", CommandName.LIST.getName(), "Command name should be 'list'");
        assertEquals("find", CommandName.FIND.getName(), "Command name should be 'find'");
        assertEquals("todo", CommandName.TODO.getName(), "Command name should be 'todo'");
        assertEquals("deadline", CommandName.DEADLINE.getName(), "Command name should be 'deadline'");
        assertEquals("event", CommandName.EVENT.getName(), "Command name should be 'event'");
        assertEquals("mark", CommandName.MARK.getName(), "Command name should be 'mark'");
        assertEquals("unmark", CommandName.UNMARK.getName(), "Command name should be 'unmark'");
        assertEquals("delete", CommandName.DELETE.getName(), "Command name should be 'delete'");
        assertEquals("edit", CommandName.EDIT.getName(), "Command name should be 'edit'");
        assertEquals("update", CommandName.UPDATE.getName(), "Command name should be 'update'");
    }
}
