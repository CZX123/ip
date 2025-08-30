package cody.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class TaskList implements Iterable<Task> {
    private final List<Task> internalList = new ArrayList<>();

    public TaskList() {}

    public TaskList(Collection<Task> tasks) {
        internalList.addAll(tasks);
    }

    public void add(Task task) {
        internalList.add(task);
    }

    public void remove(int id) {
        internalList.remove(id);
    }

    public Task get(int id) {
        return internalList.get(id);
    }

    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    public boolean isSingular() {
        return internalList.size() == 1;
    }

    public int size() {
        return internalList.size();
    }

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
