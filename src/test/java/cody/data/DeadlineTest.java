package cody.data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void testConstructor() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline = new Deadline("Submit assignment", by);
        assertEquals("Submit assignment", deadline.getDescription(), "Description should match the input");
        assertEquals(by, deadline.getBy(), "Deadline date should match the input");
    }

    @Test
    public void testGetBy() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline = new Deadline("Submit assignment", by);
        assertEquals(by, deadline.getBy(), "getBy should return the correct deadline date");
    }

    @Test
    public void testGetLetter() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline = new Deadline("Submit assignment", by);
        assertEquals('D', deadline.getLetter(), "getLetter should always return 'D' for Deadline");
    }

    @Test
    public void testFallsOn() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline = new Deadline("Submit assignment", by);

        LocalDate matchingDate = LocalDate.of(2023, 10, 15);
        LocalDate nonMatchingDate = LocalDate.of(2023, 10, 16);

        assertTrue(deadline.fallsOn(matchingDate), "fallsOn should return true for the matching date");
        assertFalse(deadline.fallsOn(nonMatchingDate), "fallsOn should return false for a non-matching date");
    }

    @Test
    public void testMarkDoneAndUnmarkDone() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline = new Deadline("Submit assignment", by);
        assertFalse(deadline.isDone(), "Deadline should initially be not done");

        deadline.markDone();
        assertTrue(deadline.isDone(), "Deadline should be marked as done");

        assertDoesNotThrow(deadline::markDone, "Marking an already done deadline should not throw");
        assertTrue(deadline.isDone(), "Deadline should still be marked as done");

        deadline.unmarkDone();
        assertFalse(deadline.isDone(), "Deadline should be unmarked and be not done");

        assertDoesNotThrow(deadline::unmarkDone, "Unmarking an already not done deadline should not throw");
        assertFalse(deadline.isDone(), "Deadline should still be not done");
    }

    @Test
    public void testToString() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline = new Deadline("Submit assignment", by);
        String expected = "[D][ ] Submit assignment (by: 15 Oct 2023 2:00PM)";
        assertEquals(expected, deadline.toString(), "toString should return the correct string representation");
    }

    @Test
    public void testEquals() {
        Deadline deadline1 = new Deadline("Submit assignment", LocalDateTime.of(2023, 10, 15, 14, 0));
        Deadline deadline2 = new Deadline("Submit assignment", LocalDateTime.of(2023, 10, 15, 14, 0));
        Deadline deadline3 = new Deadline("Different task", LocalDateTime.of(2023, 10, 15, 14, 0));
        Deadline deadline4 = new Deadline("Submit assignment", LocalDateTime.of(2023, 10, 15, 15, 0));

        assertEquals(deadline1, deadline2, "Deadlines with the same description and date should be considered equal");
        assertNotEquals(deadline1, deadline3, "Deadlines with different descriptions should not be considered equal");
        assertNotEquals(deadline1, deadline4, "Deadlines with different dates should not be considered equal");
    }

    @Test
    public void testHashCode() {
        Deadline deadline1 = new Deadline("Submit assignment", LocalDateTime.of(2023, 10, 15, 14, 0));
        Deadline deadline2 = new Deadline("Submit assignment", LocalDateTime.of(2023, 10, 15, 14, 0));

        assertEquals(deadline1.hashCode(), deadline2.hashCode(), "Equal deadlines should have the same hash code");
    }
}
