import java.util.Scanner;

public class Cody {
    public static void main(String[] args) {
        String welcomeMsg = "\n👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈\n";
        String goodbyeMsg = "👋 Bye. Hope to see you again soon! ✨";
        String divider = "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯";
        String indent = "  ";
        System.out.println(welcomeMsg + "\n" + divider + "\n");
        Scanner input = new Scanner(System.in);
        String inputTxt = input.nextLine();
        String[] list = new String[100];
        int listCount = 0;
        while (!inputTxt.equals("bye")) {
            if (inputTxt.equals("list")) {
                System.out.println();
                if (listCount == 0) System.out.println(indent + "You have no tasks for today! 😎");
                else System.out.printf("%sYou have %d task%s! 💪📝\n", indent, listCount, listCount > 1 ? "s" : "");
                for (int i = 0; i < listCount; i++) {
                    System.out.printf("%s%d. %s\n", indent, i+1, list[i]);
                }
                System.out.println();
            } else {
                list[listCount] = inputTxt;
                listCount++;
                System.out.println("\n" + indent + "added: " + inputTxt
                    + "\n\n" + divider + "\n");
            }
            inputTxt = input.nextLine();
        }
        System.out.println("\n" + divider + "\n\n" + goodbyeMsg);
    }
}
