import java.util.Scanner;

public class Cody {
    private static final String WELCOME_MSG = "\n👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈\n";
    private static final String GOODBYE_MSG = "👋 Bye. Hope to see you again soon! ✨";
    private static final String DIVIDER = "\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n";
    private static final String INDENT = "  ";

    private static final Scanner input = new Scanner(System.in);
    private static final Task[] tasks = new Task[100];
    private static int taskCount = 0;

    public static void main(String[] args) {
        System.out.println(WELCOME_MSG + DIVIDER);

        String inputTxt = input.nextLine().trim();

        while (!inputTxt.equals("bye")) {
            System.out.println();
            System.out.print(INDENT);
            if (inputTxt.equals("list")) {
                listTasks();
            } else if (inputTxt.startsWith("mark ") || inputTxt.startsWith("unmark ")) {
                markTask(inputTxt.split(" ", 2)[1], inputTxt.startsWith("mark "));
            } else if (inputTxt.startsWith("todo ") ||
                       inputTxt.startsWith("deadline ")||
                       inputTxt.startsWith("event ")) {
                addTask(inputTxt);
            } else {
                System.out.println("💣 Invalid command!");
            }
            System.out.println(DIVIDER);
            inputTxt = input.nextLine().trim();
        }

        System.out.println(DIVIDER + "\n" + GOODBYE_MSG);
    }

    private static void listTasks() {
        if (taskCount == 0) {
            System.out.println("You have no tasks for today! \uD83D\uDE0E");
        }
        else {
            System.out.printf("You have %d task%s! \uD83D\uDCAA\uD83D\uDCDD\n", taskCount, taskCount > 1 ? "s" : "");
        }
        for (int i = 0; i < taskCount; i++) {
            System.out.printf("%s%d. %s\n", INDENT, i+1, tasks[i]);
        }
    }

    private static void markTask(String taskId, boolean done) {
        int index = Integer.parseInt(taskId) - 1;
        if (index >= taskCount) {
            System.out.printf("There is no task numbered %d! \uD83D\uDE35\n", index + 1);
            return;
        }
        if (done) {
            tasks[index].markDone();
            System.out.printf("✅ Marked task as done:\n%s%s\n", INDENT, tasks[index]);
        } else {
            tasks[index].unmarkDone();
            System.out.printf("↩\uFE0F Marked task as not done:\n%s%s\n", INDENT, tasks[index]);
        }
    }

    private static void addTask(String inputTxt) {
        String taskDetails = inputTxt.split(" ", 2)[1];
        if (inputTxt.startsWith("todo ")) {
            tasks[taskCount] = new Todo(taskDetails);
        } else if (inputTxt.startsWith("deadline ")) {
            String[] split = taskDetails.split(" /by ", 2);
            tasks[taskCount] = new Deadline(split[0], split[1]);
        } else {
            String[] split1 = taskDetails.split(" /from ", 2);
            String[] split2 = split1[1].split(" /to ", 2);
            tasks[taskCount] = new Event(split1[0], split2[0], split2[1]);
        }
        System.out.println("➕ Added task:\n" + INDENT + tasks[taskCount]);
        taskCount++;
        System.out.printf("%s\uD83D\uDCCB Now there are %d task%s!\n",
                          INDENT, taskCount, taskCount > 1 ? "s" : "");
    }
}
