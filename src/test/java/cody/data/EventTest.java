package cody.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void testConstructor() {
        LocalDateTime from = LocalDateTime.of(2023, 10, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2023, 10, 16, 18, 0);
        Event event = new Event("Team meeting", from, to);

        assertEquals("Team meeting", event.getDescription(), "Description should match the input");
        assertEquals(from, event.getFrom(), "Start date should match the input");
        assertEquals(to, event.getTo(), "End date should match the input");
    }

    @Test
    public void testGetFrom() {
        LocalDateTime from = LocalDateTime.of(2023, 10, 15, 14, 0);
        Event event = new Event("Team meeting", from, LocalDateTime.of(2023, 10, 16, 18, 0));
        assertEquals(from, event.getFrom(), "getFrom should return the correct start date");
    }

    @Test
    public void testGetTo() {
        LocalDateTime to = LocalDateTime.of(2023, 10, 16, 18, 0);
        Event event = new Event("Team meeting", LocalDateTime.of(2023, 10, 15, 14, 0), to);
        assertEquals(to, event.getTo(), "getTo should return the correct end date");
    }

    @Test
    public void getLetter_alwaysReturnE() {
        Event event = new Event("Team meeting", LocalDateTime.of(2023, 10, 15, 14, 0),
                LocalDateTime.of(2023, 10, 16, 18, 0));
        assertEquals('E', event.getLetter(), "getLetter should return 'E' for Event");
    }

    @Test
    public void testFallsOn() {
        LocalDateTime from = LocalDateTime.of(2023, 10, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2023, 10, 16, 18, 0);
        Event event = new Event("Team meeting", from, to);

        LocalDate matchingDate = LocalDate.of(2023, 10, 15);
        LocalDate withinRangeDate = LocalDate.of(2023, 10, 16);
        LocalDate nonMatchingDate = LocalDate.of(2023, 10, 17);

        assertTrue(event.fallsOn(matchingDate), "fallsOn should return true for the start date");
        assertTrue(event.fallsOn(withinRangeDate), "fallsOn should return true for a date within the range");
        assertFalse(event.fallsOn(nonMatchingDate), "fallsOn should return false for a date outside the range");
    }

    @Test
    public void testToString() {
        LocalDateTime from = LocalDateTime.of(2023, 10, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2023, 10, 16, 18, 0);
        Event event = new Event("Team meeting", from, to);
        String expected = "[E][ ] Team meeting (from: 15 Oct 2023 2:00PM to: 16 Oct 2023 6:00PM)";
        assertEquals(expected, event.toString(), "toString should return the correct string representation");
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime from = LocalDateTime.of(2023, 10, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2023, 10, 16, 18, 0);
        Event event1 = new Event("Team meeting", from, to);
        Event event2 = new Event("Team meeting", from, to);
        Event event3 = new Event("Different meeting", from, to);

        assertEquals(event1, event2, "Equal events should be considered equal");
        assertNotEquals(event1, event3, "Different events should not be considered equal");
        assertEquals(event1.hashCode(), event2.hashCode(), "Equal events should have the same hash code");
        assertNotEquals(event1.hashCode(), event3.hashCode(), "Different events should have different hash codes");
    }
}
