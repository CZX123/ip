package cody.testutil;

import javafx.stage.Stage;

import cody.CodyApp;
import cody.ui.Ui;

/**
 * A stub of {@code Ui} for testing purposes.
 * Does not perform any actual UI operations, but records the method calls or messages.
 */
public class UiStub extends Ui {
    private final StringBuilder codyResponses = new StringBuilder();
    private final StringBuilder userCommands = new StringBuilder();
    private int startCallCount = 0;
    private int showWelcomeCallCount = 0;
    private int showGoodbyeCallCount = 0;

    /**
     * Returns all command responses shown so far.
     */
    public String getCodyResponses() {
        return codyResponses.toString();
    }

    /**
     * Returns all user commands shown so far.
     */
    public String getUserCommands() {
        return userCommands.toString();
    }

    /**
     * Returns the number of times the start method has been called.
     */
    public int getStartCallCount() {
        return startCallCount;
    }

    /**
     * Returns the number of times the showWelcome method has been called.
     */
    public int getShowWelcomeCallCount() {
        return showWelcomeCallCount;
    }

    /**
     * Returns the number of times the showGoodbye method has been called.
     */
    public int getShowGoodbyeCallCount() {
        return showGoodbyeCallCount;
    }

    /**
     * Increments the start call count. Does not perform any UI operations.
     */
    @Override
    public void start(CodyApp cody, Stage stage) {
        startCallCount++;
    }

    /**
     * Records the given cody message.
     * Does not perform any UI operations.
     *
     * @param message the message to record
     */
    @Override
    public void showCodyResponse(String message) {
        if (codyResponses.isEmpty()) {
            codyResponses.append(message);
        } else {
            codyResponses.append("\n").append(message);
        }
    }

    /**
     * Records the given user command.
     * Does not perform any UI operations.
     *
     * @param text the user command to record
     */
    @Override
    public void showUserCommand(String text) {
        if (userCommands.isEmpty()) {
            userCommands.append(text);
        } else {
            userCommands.append("\n").append(text);
        }
    }

    /**
     * Increments the showWelcome call count. Does not perform any UI operations.
     */
    @Override
    public void showWelcome() {
        showWelcomeCallCount++;
    }

    /**
     * Increments the showGoodbye call count. Does not perform any UI operations.
     */
    @Override
    public void showGoodbye() {
        showGoodbyeCallCount++;
    }
}
