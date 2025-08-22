import java.util.Scanner;

public class Cody {
    public static void main(String[] args) {
        String welcomeMsg = "\n👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈\n";
        String goodbyeMsg = "👋 Bye. Hope to see you again soon! ✨";
        String divider = "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯";
        String indent = "    ";
        System.out.println(welcomeMsg + "\n" + divider + "\n");
        Scanner input = new Scanner(System.in);
        String inputTxt = input.next();
        while (!inputTxt.equals("bye")) {
            System.out.println("\n" + indent + inputTxt + "\n\n" + divider + "\n");
            inputTxt = input.next();
        }
        System.out.println("\n" + divider + "\n\n" + goodbyeMsg);
    }
}
