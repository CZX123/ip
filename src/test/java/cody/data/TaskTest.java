package cody.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void testGetDescription() {
        Task task = new TaskStubOne("Test description");
        assertEquals("Test description", task.getDescription(), "Description should match the input");
    }

    @Test
    void testMarkDone() {
        Task task = new TaskStubOne("Test task");
        assertFalse(task.isDone(), "Task should initially be not done");

        task.markDone();
        assertTrue(task.isDone(), "Task should be marked as done");

        // Marking an already done task should not change its state
        task.markDone();
        assertTrue(task.isDone(), "Task should still be marked as done");
    }

    @Test
    void testUnmarkDone() {
        Task task = new TaskStubOne("Test task");
        task.markDone();
        assertTrue(task.isDone(), "Task should be marked as done");

        task.unmarkDone();
        assertFalse(task.isDone(), "Task should be unmarked and be not done");

        // Unmarking an already not done task should not change its state
        task.unmarkDone();
        assertFalse(task.isDone(), "Task should still be not done");
    }

    @Test
    void testIsDone() {
        Task task = new TaskStubOne("Test task");
        assertFalse(task.isDone(), "Task should initially be not done");

        task.markDone();
        assertTrue(task.isDone(), "Task should be marked as done");

        task.unmarkDone();
        assertFalse(task.isDone(), "Task should be unmarked and be not done");
    }

    @Test
    void testToString() {
        Task task = new TaskStubOne("Test description");
        String expected = "[S][ ] Test description";
        assertEquals(expected, task.toString(), "toString should return the correct string representation");
    }

    @Test
    void testEquals() {
        Task task1 = new TaskStubOne("Test description");
        Task task2 = new TaskStubOne("Test description");
        Task task3 = new TaskStubOne("Different description");
        Task task4 = new TaskStubTwo("Test description");

        assertEquals(task1, task2, "Tasks with the same description should be equal");
        assertNotEquals(task1, task3, "Tasks with different descriptions should not be equal");
        assertNotEquals(task1, task4, "Tasks of different subclasses should not be equal");
        assertNotEquals(task1, null, "Task should not be equal to null");
    }

    @Test
    void testHashCode() {
        Task task1 = new TaskStubOne("Test description");
        Task task2 = new TaskStubOne("Test description");
        Task task3 = new TaskStubTwo("Test description");
        assertEquals(task1.hashCode(), task2.hashCode(),
                "Tasks with the same description should have the same hash code");
        assertEquals(task1.hashCode(), task3.hashCode(),
                "Tasks of different subclasses with the same description should have the same hash code");
    }

    private static class TaskStubOne extends Task {
        public TaskStubOne(String description) {
            super(description);
        }

        @Override
        public char getLetter() {
            return 'S';
        }

        @Override
        public boolean fallsOn(LocalDate date) {
            return false;
        }
    }

    private static class TaskStubTwo extends Task {
        public TaskStubTwo(String description) {
            super(description);
        }

        @Override
        public char getLetter() {
            return 'S';
        }

        @Override
        public boolean fallsOn(LocalDate date) {
            return false;
        }
    }
}
