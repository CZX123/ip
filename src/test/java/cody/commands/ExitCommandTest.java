package cody.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ExitCommandTest {

    @Test
    void testGetName() {
        ExitCommand exitCommand = new ExitCommand();
        assertEquals("exit", exitCommand.getName(), "Command name should be 'exit'");
    }

    @Test
    void testExecute() {
        ExitCommand exitCommand = new ExitCommand();
        assertDoesNotThrow(() -> exitCommand.execute(null), "Executing ExitCommand should not throw any exception");
    }

    @Test
    void testIsExit() {
        ExitCommand exitCommand = new ExitCommand();
        assertTrue(exitCommand.isExit(), "isExit should return true for ExitCommand");
    }

    @Test
    void testEquals() {
        ExitCommand exitCommand1 = new ExitCommand();
        ExitCommand exitCommand2 = new ExitCommand();

        assertEquals(exitCommand1, exitCommand2, "Two ExitCommands should be equal");
    }

    @Test
    void testHashCode() {
        ExitCommand exitCommand1 = new ExitCommand();
        ExitCommand exitCommand2 = new ExitCommand();

        assertEquals(exitCommand1.hashCode(), exitCommand2.hashCode(), "Hash codes should match for any ExitCommand");
    }
}
