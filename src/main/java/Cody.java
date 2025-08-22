import java.util.Scanner;

public class Cody {
    private static final String WELCOME_MSG = "\n👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈\n";
    private static final String GOODBYE_MSG = "👋 Bye. Hope to see you again soon! ✨";
    private static final String DIVIDER = "\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n";
    private static final String INDENT = "  ";
    private static final Scanner input = new Scanner(System.in);
    private static final String[] tasks = new String[100];
    private static int taskCount = 0;

    public static void main(String[] args) {
        System.out.println(WELCOME_MSG + DIVIDER);

        String inputTxt = input.nextLine();

        while (!inputTxt.equals("bye")) {
            if (inputTxt.equals("list")) {
                System.out.println();
                if (taskCount == 0) {
                    System.out.println(INDENT + "You have no tasks for today! 😎");
                }
                else {
                    System.out.printf("%sYou have %d task%s! 💪📝\n",
                                      INDENT, taskCount, taskCount > 1 ? "s" : "");
                }
                for (int i = 0; i < taskCount; i++) {
                    System.out.printf("%s%d. %s\n", INDENT, i+1, tasks[i]);
                }
                System.out.println(DIVIDER);
            } else {
                tasks[taskCount] = inputTxt;
                taskCount++;
                System.out.println("\n" + INDENT + "added: " + inputTxt + "\n" + DIVIDER);
            }
            inputTxt = input.nextLine();
        }

        System.out.println(DIVIDER + "\n" + GOODBYE_MSG);
    }
}
