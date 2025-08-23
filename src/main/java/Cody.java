import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cody {
    private static final String WELCOME_MSG = "\n👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈\n";
    private static final String GOODBYE_MSG = "👋 Bye. Hope to see you again soon! ✨";
    private static final String DIVIDER = "\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n";
    private static final String INDENT = "  ";

    private static final Scanner input = new Scanner(System.in);
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(WELCOME_MSG + DIVIDER);

        String inputTxt = input.nextLine().trim();

        while (!inputTxt.equals("bye")) {
            System.out.println();
            System.out.print(INDENT);
            try {
                switch (Command.of(inputTxt)) {
                    case LIST -> listTasks();
                    case TODO -> addTodo(inputTxt);
                    case DEADLINE -> addDeadline(inputTxt);
                    case EVENT -> addEvent(inputTxt);
                    case MARK, UNMARK -> markTask(inputTxt);
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

    private static void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("You have no tasks for today! \uD83D\uDE0E");
        } else {
            System.out.printf("You have %d task%s! \uD83D\uDCAA\uD83D\uDCDD\n",
                tasks.size(), tasks.size() == 1 ? "" : "s");
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%s%d. %s\n", INDENT, i+1, tasks.get(i));
        }
    }

    private static void markTask(String inputTxt) throws CodyException {
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
        if (inputTxt.startsWith("mark")) {
            tasks.get(index).markDone();
            System.out.printf("✅ Marked task as done:\n%s%s\n", INDENT, tasks.get(index));
        } else {
            tasks.get(index).unmarkDone();
            System.out.printf("↩\uFE0F Marked task as not done:\n%s%s\n", INDENT, tasks.get(index));
        }
    }

    private static void printTaskAmount() {
        System.out.printf("\n%s\uD83D\uDCCB Now there %s %d task%s!\n",
            INDENT, tasks.size() == 1 ? "is" : "are", tasks.size(), tasks.size() == 1 ? "" : "s");
    }

    private static void addTodo(String inputTxt) throws CodyException {
        String[] split = inputTxt.split(" ", 2);
        String command = split[0];
        if (!command.equals("todo")) throw new CodyException("⚠\uFE0F Invalid command!");
        if (split.length < 2) throw new CodyException("The description of a todo cannot be empty!");
        Task task = new Todo(split[1]);
        tasks.add(task);
        System.out.println("➕ Added task:\n   " + INDENT + task);
        printTaskAmount();
    }

    private static void addDeadline(String inputTxt) throws CodyException {
        if (!inputTxt.matches("deadline .+ /by .+")) {
            throw new CodyException("Deadlines should follow this format:\n"
                + INDENT + "deadline <description> /by <due date>");
        }
        String[] split = inputTxt.split(" ", 2)[1].split(" /by ", 2);
        Task task = new Deadline(split[0], split[1]);
        tasks.add(task);
        System.out.println("➕ Added task:\n   " + INDENT + task);
        printTaskAmount();
    }

    private static void addEvent(String inputTxt) throws CodyException {
        if (!inputTxt.matches("event .+ /from .+ /to .+")) {
            throw new CodyException("Events should follow this format:\n"
                + INDENT + "event <description> /from <start date> /to <end date>");
        }
        String[] fromSplit = inputTxt.split(" ", 2)[1].split(" /from ", 2);
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        Task task = new Event(fromSplit[0], toSplit[0], toSplit[1]);
        tasks.add(task);
        System.out.println("➕ Added task:\n   " + INDENT + task);
        printTaskAmount();
    }

    private static void deleteTask(String inputTxt) throws CodyException {
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
        Task task = tasks.get(index);
        tasks.remove(index);
        System.out.println("\uD83E\uDDFA Deleted task:\n   " + INDENT + task);
        printTaskAmount();
    }
}
