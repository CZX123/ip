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

    public static Task fromString(String str) throws CodyException {
        return switch (str.charAt(1)) {
            case 'T' -> Todo.fromString(str);
            case 'D' -> Deadline.fromString(str);
            case 'E' -> Event.fromString(str);
            default -> throw new CodyException("Invalid task format!");
        };
    }
}
