package cody.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import cody.testutil.TaskStub;

public class TaskListTest {

    @Test
    public void constructor_emptyList_constructsEmptyTaskList() {
        TaskList taskList = new TaskList();
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.size());
    }

    @Test
    public void constructor_nonEmptyList_constructsTaskListCorrectly() {
        Task task1 = new TaskStub("Task 1");
        Task task2 = new TaskStub("Task 2");
        TaskList taskList = new TaskList(List.of(task1, task2));

        assertFalse(taskList.isEmpty());
        assertEquals(2, taskList.size());
        assertEquals(task1, taskList.get(0));
        assertEquals(task2, taskList.get(1));
    }

    @Test
    public void addTask_emptyList_increasesSize() {
        TaskList taskList = new TaskList();
        Task task = new TaskStub("Sample Task");

        taskList.add(task);

        assertEquals(1, taskList.size());
        assertEquals(task, taskList.get(0));
    }

    @Test
    public void addTaskAtSpecificIndex_nonEmptyList_insertsCorrectly() {
        TaskList taskList = new TaskList();
        Task task1 = new TaskStub("Task 1");
        Task task2 = new TaskStub("Task 2");

        taskList.add(task1);
        taskList.add(0, task2);

        assertEquals(2, taskList.size());
        assertEquals(task2, taskList.get(0));
        assertEquals(task1, taskList.get(1));
    }

    @Test
    public void removeTask_existingTask_decreasesSize() {
        Task task = new TaskStub("Sample Task");
        TaskList taskList = new TaskList(List.of(task));

        taskList.remove(0);

        assertTrue(taskList.isEmpty());
    }

    @Test
    public void getTask_validIndex_returnsCorrectTask() {
        Task task = new TaskStub("Sample Task");
        TaskList taskList = new TaskList(List.of(task));

        assertEquals(task, taskList.get(0));
    }

    @Test
    public void isEmpty_emptyList_returnsTrue() {
        TaskList taskList = new TaskList();

        assertTrue(taskList.isEmpty());
    }

    @Test
    public void isEmpty_nonEmptyList_returnsFalse() {
        TaskList taskList = new TaskList(List.of(new TaskStub("Sample Task")));

        assertFalse(taskList.isEmpty());
    }

    @Test
    public void isSingular_singleTask_returnsTrue() {
        TaskList taskList = new TaskList(List.of(new TaskStub("Single Task")));

        assertTrue(taskList.isSingular());
    }

    @Test
    public void isSingular_multipleTasks_returnsFalse() {
        TaskList taskList = new TaskList(List.of(new TaskStub("Task 1"), new TaskStub("Task 2")));

        assertFalse(taskList.isSingular());
    }

    @Test
    public void filter_matchingTasks_returnsFilteredTaskList() {
        Task task1 = new TaskStub("Task 1");
        Task task2 = new TaskStub("Task 2");
        TaskList taskList = new TaskList(List.of(task1, task2));

        TaskList filteredList = taskList.filter(task -> task.getDescription().equals("Task 1"));

        assertEquals(1, filteredList.size());
        assertEquals(task1, filteredList.get(0));
    }

    @Test
    public void filter_noMatchingTasks_returnsEmptyList() {
        TaskList taskList = new TaskList(List.of(new TaskStub("Task 1"), new TaskStub("Task 2")));

        TaskList filteredList = taskList.filter(task -> task.getDescription().equals("Nonexistent Task"));

        assertTrue(filteredList.isEmpty());
    }

    @Test
    public void toString_nonEmptyList_returnsFormattedString() {
        Task task1 = new TaskStub("Task 1");
        Task task2 = new TaskStub("Task 2");
        TaskList taskList = new TaskList(List.of(task1, task2));

        String expected = "1. [Stub] Task 1\n2. [Stub] Task 2";
        assertEquals(expected, taskList.toString());
    }

    @Test
    public void toString_emptyList_returnsEmptyString() {
        TaskList taskList = new TaskList();

        assertEquals("", taskList.toString());
    }

    @Test
    public void equals_equalTaskLists_returnsTrue() {
        Task task = new TaskStub("Sample Task");
        TaskList taskList1 = new TaskList(List.of(task));
        TaskList taskList2 = new TaskList(List.of(task));

        assertEquals(taskList1, taskList2);
    }

    @Test
    public void equals_differentTaskLists_returnsFalse() {
        TaskList taskList1 = new TaskList(List.of(new TaskStub("Task 1")));
        TaskList taskList2 = new TaskList(List.of(new TaskStub("Task 2")));

        assertNotEquals(taskList1, taskList2);
    }

    @Test
    public void hashCode_equalTaskLists_consistentWithEquals() {
        Task task = new TaskStub("Sample Task");
        TaskList taskList1 = new TaskList(List.of(task));
        TaskList taskList2 = new TaskList(List.of(task));

        assertEquals(taskList1.hashCode(), taskList2.hashCode());
    }
}
