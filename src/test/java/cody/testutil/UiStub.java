package cody.testutil;

import cody.ui.Ui;

/**
 * A stub of {@code Ui} for testing purposes.
 * Records messages shown to the user for verification in tests.
 */
public class UiStub extends Ui {
    private final StringBuilder messages;

    public UiStub() {
        this.messages = new StringBuilder();
    }

    public void showCodyResponse(String message) {
        messages.append(message).append("\n");
    }

    public String getMessages() {
        return messages.toString().trim();
    }
}
