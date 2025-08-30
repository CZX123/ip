package cody.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * A list of tasks.
 */
public class TaskList implements Iterable<Task> {
    private final List<Task> internalList = new ArrayList<>();

    /**
     * Constructs an empty task list.
     */
    public TaskList() {}

    /**
     * Constructs a task list that contains the given initial tasks.
     *
     * @param tasks a collection of initial tasks
     */
    public TaskList(Collection<Task> tasks) {
        internalList.addAll(tasks);
    }

    /**
     * Adds a task to the list.
     */
    public void add(Task task) {
        internalList.add(task);
    }


    /**
     * Removes a task from the list based on given id.
     */
    public void remove(int id) {
        internalList.remove(id);
    }

    /**
     * Retrieves a task from the list based on given id.
     */
    public Task get(int id) {
        return internalList.get(id);
    }

    /**
     * Returns whether task list is empty.
     */
    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    /**
     * Returns whether task list only contains one task.
     */
    public boolean isSingular() {
        return internalList.size() == 1;
    }

    /**
     * Returns size of the task list.
     */
    public int size() {
        return internalList.size();
    }

    /**
     * Filters the task list.
     *
     * @param predicate filter condition
     * @return a new {@code TaskList} with the tasks filtered
     */
    public TaskList filter(Predicate<Task> predicate) {
        return new TaskList(internalList.stream().filter(predicate).toList());
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public void forEach(Consumer<? super Task> action) {
        internalList.forEach(action);
    }

    @Override
    public String toString() {
        return IntStream.range(0, internalList.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, internalList.get(i)))
                .reduce((a, b) -> a + "\n" + b).orElse("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskList tasks = (TaskList) o;
        return Objects.equals(internalList, tasks.internalList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalList);
    }
}
