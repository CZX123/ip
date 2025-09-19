package cody.ui;

import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import cody.CodyApp;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    private CodyApp codyApp;
    private int childrenCount = 0;

    /**
     * Initializes the main window.
     */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener(this::handleHeightChange);
    }

    /**
     * Scrolls {@code scrollPane} to bottom when height of content changes and
     * number of child nodes are different.
     */
    private void handleHeightChange(Observable ignored) {
        int size = dialogContainer.getChildrenUnmodifiable().size();
        if (size == childrenCount) {
            return;
        }
        childrenCount = size;
        scrollPane.setVvalue(1);
    }

    /**
     * Updates the main window with the main CodyApp instance.
     *
     * @param c the main CodyApp.
     */
    public void setCody(CodyApp c) {
        codyApp = c;
    }

    /**
     * Inserts a node to the dialog container.
     *
     * @param node the node to insert.
     */
    @FXML
    public void insertNode(Node node) {
        dialogContainer.getChildren().add(node);
    }

    /**
     * Handles user input.
     * Should be called when user presses "Enter" or clicks the send button.
     */
    @FXML
    public void handleUserInput() {
        String text = userInput.getText().trim();
        if (text.isEmpty()) {
            return;
        }
        codyApp.respond(text);
        userInput.clear();
    }
}
