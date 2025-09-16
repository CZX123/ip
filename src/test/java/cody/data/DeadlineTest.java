package cody.data;

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
    public void getLetter_alwaysReturnD() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline = new Deadline("Submit assignment", by);
        assertEquals('D', deadline.getLetter(), "getLetter should return 'D' for Deadline");
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
    public void testToString() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline = new Deadline("Submit assignment", by);
        String expected = "[D][ ] Submit assignment (by: 15 Oct 2023 2:00PM)";
        assertEquals(expected, deadline.toString(), "toString should return the correct string representation");
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime by = LocalDateTime.of(2023, 10, 15, 14, 0);
        Deadline deadline1 = new Deadline("Submit assignment", by);
        Deadline deadline2 = new Deadline("Submit assignment", by);
        Deadline deadline3 = new Deadline("Different task", by);

        assertEquals(deadline1, deadline2, "Equal deadlines should be considered equal");
        assertNotEquals(deadline1, deadline3, "Different deadlines should not be considered equal");
        assertEquals(deadline1.hashCode(), deadline2.hashCode(), "Equal deadlines should have the same hash code");
        assertNotEquals(deadline1.hashCode(), deadline3.hashCode(),
                "Different deadlines should have different hash codes");
    }
}
