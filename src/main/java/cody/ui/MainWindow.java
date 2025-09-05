package cody.ui;

import cody.CodyApp;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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

    /**
     * Initializes the main window.
     */
    @FXML
    public void initialize() {
        // Auto-scrolls scrollPane to bottom when height of content changes
        dialogContainer.heightProperty().addListener(ignored -> scrollPane.setVvalue(1));
    }

    /**
     * Updates the main window with the main CodyApp instance.
     *
     * @param c the main CodyApp
     */
    public void setCody(CodyApp c) {
        codyApp = c;
    }

    /**
     * Inserts a node at the bottom of the list.
     *
     * @param node the node to insert
     */
    @FXML
    public void insertNode(Node node) {
        dialogContainer.getChildren().add(node);
    }

    /**
     * Handles user input. Called when user presses "Enter" or clicks the send button.
     */
    @FXML
    public void handleUserInput() {
        String text = userInput.getText().trim();
        if (text.isEmpty()) {
            return;
        }
        codyApp.run(text);
        userInput.clear();
    }
}
