package cody.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import cody.CodyApp;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Handles output display.
 */
public class Ui {
    private static final String WELCOME_MSG = "Hello! I'm Cody. \nWhat can I do for you?";
    private static final String GOODBYE_MSG = "Bye. Hope to see you again soon!";

    private MainWindow mainWindow;
    private Font font;

    /**
     * Starts the application and loads the UI.
     *
     * @param stage the main stage of the application
     */
    public void start(CodyApp cody, Stage stage) {
        Parent mainNode;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
            mainNode = fxmlLoader.load();
            mainWindow = fxmlLoader.getController();
            InputStream fontStream = getClass().getResourceAsStream("/fonts/ubuntu-mono.ttf");
            font = Font.loadFont(fontStream, 14);
            mainWindow.setCody(cody);
            Scene scene = new Scene(mainNode);
            stage.setScene(scene);
            stage.setTitle("Cody");
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cody");
            alert.setHeaderText("An error has occurred!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            close();
        }
    }

    /**
     * Closes the application.
     */
    public void close() {
        Platform.exit();
    }

    /**
     * Displays horizontal divider.
     */
    public void showDivider() {
        mainWindow.insertNode(new Separator());
    }

    /**
     * Displays text.
     */
    public void showText(String text) {
        Label label = new Label(text);
        label.setFont(font);
        label.setWrapText(true);
        label.minWidth(100);
        mainWindow.insertNode(label);
    }

    /**
     * Displays welcome message.
     */
    public void showWelcome() {
        showText(WELCOME_MSG);
        showDivider();
    }

    /**
     * Displays error message.
     *
     * @param message the error message
     */
    public void showError(String message) {
        showText(message);
        showDivider();
    }

    /**
     * Displays goodbye message.
     */
    public void showGoodbye() {
        showDivider();
        showText(GOODBYE_MSG);
    }

    /**
     * Displays result from command execution.
     *
     * @param text the output from command execution
     */
    public void showCommandResult(String text) {
        showText(text);
        showDivider();
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
}
