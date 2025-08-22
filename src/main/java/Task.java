abstract class Task {
    private final String name;
    private boolean done = false;

    public Task(String desc) {
        this.name = desc;
    }

    public void markDone() {
        done = true;
    }

    public void unmarkDone() {
        done = false;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", done ? "X" : " ", name);
    }
}
