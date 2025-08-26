import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cody {
    public static final String INDENT = "  ";

    private static final String WELCOME_MSG = "\n👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈\n";
    private static final String GOODBYE_MSG = "👋 Bye. Hope to see you again soon! ✨";
    private static final String DIVIDER = "\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n";

    private static final File file = new File("data/tasks.txt");
    private static final Scanner input = new Scanner(System.in);
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(WELCOME_MSG + DIVIDER);
        loadTasks();

        String inputTxt = input.nextLine().trim();
        while (!inputTxt.equals("bye")) {
            System.out.println();
            System.out.print(INDENT);
            try {
                Command command = Command.of(inputTxt);
                switch (command) {
                    case LIST -> listTasks();
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

    private static void loadTasks() {
        try {
            Scanner s = new Scanner(file);
            StringBuilder corruptedData = new StringBuilder();
            while (s.hasNext()) {
                String line = s.nextLine();
                try {
                    tasks.add(Task.fromString(line));
                } catch (CodyException e) {
                    corruptedData.append("   ").append(line).append("\n");
                }
            }
            if (!corruptedData.isEmpty()) {
                System.out.println("\uD83D\uDCA5 There's something wrong with the saved data below: \n");
                System.out.println(corruptedData);
                System.out.println("\uD83D\uDD04 Please re-add the tasks manually.");
                System.out.println(DIVIDER);
            }
        } catch (FileNotFoundException ignored) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch(IOException e) {
                System.out.println("Error while creating save file: " + e.getMessage());
                System.out.println(DIVIDER);
            }
        }
    }

    private static void saveTasks() {
        StringBuilder taskData = new StringBuilder();
        for (Task task : tasks) {
            taskData.append(task.toString()).append("\n");
        }
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(taskData.toString());
            fw.close();
        } catch (IOException e) {
            System.out.println("\nError while saving tasks: " + e.getMessage());
        }
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
        saveTasks();
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
        saveTasks();
    }

    private static void deleteTask(String inputTxt) throws CodyException {
        int index = getTaskIndex(inputTxt);
        Task task = tasks.get(index);
        tasks.remove(index);
        System.out.println("\uD83E\uDDFA Deleted task:\n   " + INDENT + task);
        printTaskAmount();
        saveTasks();
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
            throw new CodyException(String.format(
                    "There is no task numbered %d! \uD83D\uDE35", index + 1));
        }
        return index;
    }

    private static void printTaskAmount() {
        System.out.printf("\n%s\uD83D\uDCCB Now there %s %d task%s!\n",
                INDENT, tasks.size() == 1 ? "is" : "are",
                tasks.size(), tasks.size() == 1 ? "" : "s");
    }
}
