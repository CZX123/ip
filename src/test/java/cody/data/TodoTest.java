package cody.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void testConstructor() {
        Todo todo = new Todo("Test description");
        assertEquals("Test description", todo.getDescription(), "Description should match the input");
    }

    @Test
    public void testGetLetter() {
        Todo todo = new Todo("Test description");
        assertEquals('T', todo.getLetter(), "Todo should always return 'T' as its letter");
    }

    @Test
    public void testFallsOn() {
        Todo todo = new Todo("Test description");
        assertFalse(todo.fallsOn(null), "Todo should always return false for fallsOn");
    }

    @Test
    public void testToString() {
        Todo todo = new Todo("Test description");
        String expected = "[T][ ] Test description";
        assertEquals(expected, todo.toString(), "toString should return the correct string representation");
    }

    @Test
    public void testEquals() {
        Todo todo1 = new Todo("Test description");
        Todo todo2 = new Todo("Test description");
        Todo todo3 = new Todo("Different description");

        assertEquals(todo1, todo2, "Todos with the same description should be equal");
        assertNotEquals(todo1, todo3, "Todos with different descriptions should not be equal");
    }

    @Test
    public void testHashCode() {
        Todo todo1 = new Todo("Test description");
        Todo todo2 = new Todo("Test description");

        assertEquals(todo1.hashCode(), todo2.hashCode(), "Hash codes should match for equal Todos");
    }
}
