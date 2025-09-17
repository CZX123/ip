package cody.commands.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import cody.data.TaskList;
import cody.storage.Storage;
import cody.ui.Ui;

class CommandTest {

    private static class CommandStubOne extends Command {
        public CommandStubOne() {
            super("same name");
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            // No-op for testing purposes
        }

        @Override
        public boolean isExit() {
            return false;
        }
    }

    private static class CommandStubTwo extends Command {
        public CommandStubTwo() {
            super("same name");
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            // No-op for testing purposes
        }

        @Override
        public boolean isExit() {
            return false;
        }
    }

    @Test
    void testGetName() {
        Command command1 = new Command("test") {
            @Override
            public void execute(TaskList tasks, Ui ui, Storage storage) {
                // No-op for testing purposes
            }

            @Override
            public boolean isExit() {
                return false;
            }
        };
        assertEquals("test", command1.getName(), "Command name should match the provided value");

        Command command2 = new CommandStubOne();
        assertEquals("same name", command2.getName(), "Command name should match the provided value");
    }

    @Test
    void testEquals() {
        Command command1 = new CommandStubOne();
        Command command2 = new CommandStubOne();
        Command command3 = new CommandStubTwo();
        Command command4 = new Command("different name") {
            @Override
            public void execute(TaskList tasks, Ui ui, Storage storage) {
                // No-op for testing purposes
            }

            @Override
            public boolean isExit() {
                return false;
            }
        };

        assertEquals(command1, command1, "Command should be equal to itself");
        assertEquals(command1, command2, "Commands with the same class and name should be equal");
        assertNotEquals(command1, command4, "Commands with different names should not be equal");
        assertNotEquals(command1, command3, "Commands of different classes should not be equal");
        assertNotEquals(command1, null, "Command should not be equal to null");
    }

    @Test
    void testHashCode() {
        Command command1 = new CommandStubOne();
        Command command2 = new CommandStubOne();
        Command command3 = new CommandStubTwo();

        assertEquals(command1.hashCode(), command2.hashCode(), "Equal commands should have the same hash code");
        assertEquals(command1.hashCode(), command3.hashCode(),
                "Commands with different classes but the same name should still have the same hash code");
    }
}
