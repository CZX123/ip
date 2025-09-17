package cody.data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class EventTest {

    private LocalDateTime getDefaultStartDate() {
        return LocalDateTime.of(2023, 10, 15, 14, 0);
    }

    private LocalDateTime getDefaultEndDate() {
        return LocalDateTime.of(2023, 10, 17, 18, 0);
    }

    @Test
    public void testConstructor() {
        Event event = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());

        assertEquals("Team meeting", event.getDescription(), "Description should match the input");
        assertEquals(getDefaultStartDate(), event.getFrom(), "Start date should match the input");
        assertEquals(getDefaultEndDate(), event.getTo(), "End date should match the input");
    }

    @Test
    public void testGetFrom() {
        Event event = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());
        assertEquals(getDefaultStartDate(), event.getFrom(), "getFrom should return the correct start date");
    }

    @Test
    public void testGetTo() {
        Event event = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());
        assertEquals(getDefaultEndDate(), event.getTo(), "getTo should return the correct end date");
    }

    @Test
    public void testGetLetter() {
        Event event = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());
        assertEquals('E', event.getLetter(), "getLetter should always return 'E' for Event");
    }

    @Test
    public void testFallsOn() {
        Event event = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());

        LocalDate beforeStartDate = LocalDate.of(2023, 10, 13);
        LocalDate matchingStartDate = LocalDate.of(2023, 10, 15);
        LocalDate withinRangeDate = LocalDate.of(2023, 10, 16);
        LocalDate matchingEndDate = LocalDate.of(2023, 10, 17);
        LocalDate afterEndDate = LocalDate.of(2023, 10, 18);

        assertFalse(event.fallsOn(beforeStartDate), "fallsOn should return false for a date before the range");
        assertTrue(event.fallsOn(matchingStartDate), "fallsOn should return true for the start date");
        assertTrue(event.fallsOn(withinRangeDate), "fallsOn should return true for a date within the range");
        assertTrue(event.fallsOn(matchingEndDate), "fallsOn should return true for the end date");
        assertFalse(event.fallsOn(afterEndDate), "fallsOn should return false for a date after the range");
    }

    @Test
    public void testMarkDoneAndUnmarkDone() {
        Event event = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());
        assertFalse(event.isDone(), "Event should initially be not done");

        event.markDone();
        assertTrue(event.isDone(), "Event should be marked as done");

        assertDoesNotThrow(event::markDone, "Marking an already done event should not throw");
        assertTrue(event.isDone(), "Event should still be marked as done");

        event.unmarkDone();
        assertFalse(event.isDone(), "Event should be unmarked and be not done");

        assertDoesNotThrow(event::unmarkDone, "Unmarking an already not done event should not throw");
        assertFalse(event.isDone(), "Event should still be not done");
    }

    @Test
    public void testToString() {
        Event event = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());
        String expected = "[E][ ] Team meeting (from: 15 Oct 2023 2:00PM to: 17 Oct 2023 6:00PM)";
        assertEquals(expected, event.toString(), "toString should return the correct string representation");
    }

    @Test
    public void testEquals() {
        Event event1 = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());
        Event event2 = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());
        Event event3 = new Event("Different meeting", getDefaultStartDate(), getDefaultEndDate());
        Event event4 = new Event("Team meeting", LocalDateTime.of(2023, 10, 15, 12, 0), getDefaultEndDate());
        Event event5 = new Event("Team meeting", getDefaultStartDate(), LocalDateTime.of(2023, 10, 16, 18, 0));

        assertEquals(event1, event2, "Events with the same descriptions and dates should be considered equal");
        assertNotEquals(event1, event3, "Events with different descriptions should not be considered equal");
        assertNotEquals(event1, event4, "Events with different from dates should not be considered equal");
        assertNotEquals(event1, event5, "Events with different to dates should not be considered equal");
    }

    @Test
    public void testHashCode() {
        Event event1 = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());
        Event event2 = new Event("Team meeting", getDefaultStartDate(), getDefaultEndDate());

        assertEquals(event1.hashCode(), event2.hashCode(), "Equal events should have the same hash code");
    }
}
