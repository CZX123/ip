import java.util.Scanner;

public class Cody {
    public static final String INDENT = "  ";

    private static final String WELCOME_MSG = "\n👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈\n";
    private static final String GOODBYE_MSG = "👋 Bye. Hope to see you again soon! ✨";
    private static final String DIVIDER = "\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n";

    private static final Storage storage = new Storage();
    private static final Scanner input = new Scanner(System.in);
    private static TaskList tasks;

    public static void main(String[] args) {
        System.out.println(WELCOME_MSG + DIVIDER);
        try {
            tasks = storage.load();
        } catch (Storage.StorageOperationException e) {
            System.out.println(e.getMessage());
            System.out.println(DIVIDER);
            tasks = new TaskList();
        }

        String inputTxt = input.nextLine().trim();
        while (!inputTxt.equals("bye")) {
            System.out.println();
            System.out.print(INDENT);
            try {
                Command command = Command.of(inputTxt);
                switch (command) {
                    case LIST -> listTasks(inputTxt);
                    case TODO, DEADLINE, EVENT -> addTask(command, inputTxt);
                    case MARK, UNMARK -> markTask(command, inputTxt);
                    case DELETE -> deleteTask(inputTxt);
                    default -> throw new CodyException("⚠\uFE0F Invalid command!");
                }
            } catch (CodyException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(DIVIDER);
            inputTxt = input.nextLine().trim();
        }

        System.out.println(DIVIDER + "\n" + GOODBYE_MSG);
    }

    // Temporary method for use in Ui class later
    private static String indent(String text, String indent) {
        text = indent + text.replaceAll("\n", "\n" + indent);
        if (text.endsWith(indent)) {
            text = text.substring(0, text.length() - indent.length());
        }
        return text;
    }

    private static void listTasks(String inputTxt) {
        boolean hasDateFilter = !inputTxt.trim().equals("list");
        if (tasks.isEmpty()) {
                System.out.println("You have no tasks saved! \uD83D\uDE0E");
        } else {
            System.out.printf("You have %d task%s! \uD83D\uDCAA\uD83D\uDCDD\n",
                    tasks.size(), tasks.isSingular() ? "" : "s");
        }
        System.out.print(indent(tasks.toString(), INDENT));
    }

    private static void addTask(Command command, String inputTxt) throws CodyException {
        Task task;
        switch (command) {
            case TODO -> task = Todo.fromCommand(inputTxt);
            case DEADLINE -> task = Deadline.fromCommand(inputTxt);
            case EVENT -> task = Event.fromCommand(inputTxt);
            default -> throw new Error("addTask called incorrectly!"); // should never happen
        }
        tasks.add(task);
        System.out.println("➕ Added task:\n   " + INDENT + task);
        printTaskAmount();
        storage.save(tasks);
    }

    private static void markTask(Command command, String inputTxt) throws CodyException {
        int index = getTaskIndex(inputTxt);
        if (command == Command.MARK) {
            tasks.get(index).markDone();
            System.out.printf("✅ Marked task as done:\n%s%s\n", INDENT, tasks.get(index));
        } else {
            tasks.get(index).unmarkDone();
            System.out.printf("↩\uFE0F Marked task as not done:\n%s%s\n", INDENT, tasks.get(index));
        }
        storage.save(tasks);
    }

    private static void deleteTask(String inputTxt) throws CodyException {
        int index = getTaskIndex(inputTxt);
        Task task = tasks.get(index);
        tasks.remove(index);
        System.out.println("\uD83E\uDDFA Deleted task:\n   " + INDENT + task);
        printTaskAmount();
        storage.save(tasks);
    }

    private static int getTaskIndex(String inputTxt) throws CodyException {
        int index;
        try {
            index = Integer.parseInt(inputTxt.split(" ", 2)[1]) - 1;
        } catch (Exception e) {
            throw new CodyException("Please enter a valid task number! \uD83E\uDD74\n"
                    + INDENT + "To view task number, type \"list\".");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new CodyException(String.format("There is no task numbered %d! \uD83D\uDE35", index + 1));
        }
        return index;
    }

    private static void printTaskAmount() {
        System.out.printf("\n%s\uD83D\uDCCB Now there %s %d task%s!\n", INDENT, tasks.size() == 1 ? "is" : "are",
                tasks.size(), tasks.size() == 1 ? "" : "s");
    }
}
