package cody.ui;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Handles output display.
 */
public class Ui {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String INDENT = "  ";
    private static final String WELCOME_MSG = "👋 Hello! I'm Cody. 🤖\nWhat can I do for you? 🌈";
    private static final String GOODBYE_MSG = "👋 Bye. Hope to see you again soon! ✨";
    private static final String DIVIDER_SYMBOL = "⎯";

    private final Scanner input = new Scanner(System.in);

    /**
     * Displays welcome message.
     */
    public void showWelcome() {
        System.out.println();
        System.out.println(WELCOME_MSG);
        showDivider(longestLength(WELCOME_MSG));
    }

    /**
     * Displays error message in red.
     *
     * @param message the error message
     */
    public void showError(String message) {
        String output = indent(message);
        System.out.println();
        System.out.println(ANSI_RED + output + ANSI_RESET);
        showDivider(longestLength(output));
    }

    /**
     * Displays goodbye message.
     */
    public void showGoodbye() {
        showDivider(longestLength(GOODBYE_MSG));
        System.out.println(GOODBYE_MSG);
    }

    /**
     * Reads user input.
     *
     * @return the full command from the user input
     */
    public String readCommand() {
        return input.nextLine().trim();
    }

    /**
     * Displays result from command execution.
     *
     * @param text the output from command execution
     */
    public void showCommandResult(String text) {
        String output = indent(text); // Apply indent to make it look better
        System.out.println();
        System.out.println(output);
        showDivider(longestLength(output));
    }

    /**
     * Removes the numbering that appears by default when displaying task list.
     * This is needed when listing tasks through a filter, as the number may be
     * different from the actual task id, which may confuse users.
     *
     * @param text the string representation of task list
     * @return task list string representation without numbering
     */
    public String removeNumberingFromTasks(String text) {
        return Arrays.stream(text.split("\n")).map(line -> line.substring(line.indexOf('.') + 2))
                .reduce((a, b) -> a + "\n" + b).orElse("");
    }

    /**
     * Adds a fixed-size indent at the start of every line in the given text.
     *
     * @param text text which may have multiple lines
     * @return text with the indent added to each line
     */
    private String indent(String text) {
        text = INDENT + text.replaceAll("\n", "\n" + INDENT);
        if (text.endsWith(INDENT)) {
            text = text.substring(0, text.length() - INDENT.length());
        }
        return text;
    }

    /**
     * Gets the length of the longest line from the given text.
     *
     * @param text text which may have multiple lines
     * @return length of the longest line
     */
    private int longestLength(String text) {
        return Arrays.stream(text.split("\n")).map(String::length).reduce(0, Math::max);
    }

    /**
     * Displays the divider.
     * Also adds extra spaces above and below the divider for better separation.
     *
     * @param length length of the divider
     */
    private void showDivider(int length) {
        System.out.println("\n" + DIVIDER_SYMBOL.repeat(length) + "\n");
    }
}
