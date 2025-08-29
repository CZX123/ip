import java.util.Arrays;
import java.util.Scanner;

public class Ui {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String INDENT = "  ";
    private static final String WELCOME_MSG = "👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈";
    private static final String GOODBYE_MSG = "👋 Bye. Hope to see you again soon! ✨";
    private static final char DIVIDER_CHAR = '⎯';
    private static final String DIVIDER = "\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n";

    private final Scanner input = new Scanner(System.in);

    public void showWelcome() {
        System.out.println();
        System.out.println(WELCOME_MSG);
        showDivider(longestLength(WELCOME_MSG));
    }

    public void showError(String message) {
        String output = indent(message);
        System.out.println();
        System.out.println(ANSI_RED + output + ANSI_RESET);
        showDivider(longestLength(output));
    }

    public void showGoodbye() {
        showDivider(longestLength(GOODBYE_MSG));
        System.out.println(GOODBYE_MSG);
    }

    public String readCommand() {
        return input.nextLine().trim();
    }

    public void showCommandResult(String text) {
        String output = indent(text);
        System.out.println();
        System.out.println(output);
        showDivider(longestLength(output));
    }

    private String indent(String text) {
        text = INDENT + text.replaceAll("\n", "\n" + INDENT);
        if (text.endsWith(INDENT)) {
            text = text.substring(0, text.length() - INDENT.length());
        }
        return text;
    }

    private int longestLength(String text) {
        return Arrays.stream(text.split("\n")).map(String::length).reduce(0, Math::max);
    }

    private void showDivider(int length) {
        System.out.println("\n" + String.valueOf(DIVIDER_CHAR).repeat(length) + "\n");
    }
}
