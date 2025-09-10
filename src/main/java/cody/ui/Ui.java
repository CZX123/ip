package cody.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import cody.CodyApp;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Handles output display.
 */
public class Ui {
    private static final String WELCOME_MSG = "Hello! I'm Cody. \nWhat can I do for you?";
    private static final String GOODBYE_MSG = "Bye. Hope to see you again soon!";

    private static Ui instance;

    private MainWindow mainWindow;
    private Image codyImage;
    private Image userImage;

    private Ui() {}

    /**
     * Gets the currently active {@code Ui} instance.
     */
    public static Ui getInstance() {
        if (instance == null) {
            instance = new Ui();
        }
        return instance;
    }

    /**
     * Starts the application and loads the UI.
     *
     * @param stage the main stage of the application
     */
    public void start(CodyApp cody, Stage stage) {
        Parent mainNode;
        try {
            URL fxmlUrl = getClass().getResource("/view/MainWindow.fxml");
            InputStream fontStream = getClass().getResourceAsStream("/fonts/ubuntu-mono.ttf");
            InputStream codyImageStream = getClass().getResourceAsStream("/images/cody.png");
            InputStream userImageStream = getClass().getResourceAsStream("/images/user.png");

            assert fxmlUrl != null;
            assert fontStream != null;
            assert codyImageStream != null;
            assert userImageStream != null;

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            mainNode = fxmlLoader.load();
            mainWindow = fxmlLoader.getController();
            mainWindow.setCody(cody);

            Font.loadFont(fontStream, 14);
            codyImage = new Image(codyImageStream);
            userImage = new Image(userImageStream);

            Scene scene = new Scene(mainNode);
            stage.setScene(scene);
            stage.setTitle("Cody");
            stage.getIcons().add(codyImage);
            stage.show();
        } catch (IOException e) {
            showFatalError(e.getMessage());
        }
    }

    /**
     * Closes the application.
     */
    public void close() {
        Platform.exit();
    }

    /**
     * Displays Cody's response to user command.
     */
    public void showCodyResponse(String text) {
        DialogBox dialog = DialogBox.getCodyDialog(text, codyImage);
        mainWindow.insertNode(dialog);
    }

    /**
     * Displays user's command.
     */
    public void showUserCommand(String text) {
        DialogBox dialog = DialogBox.getUserDialog(text, userImage);
        mainWindow.insertNode(dialog);
    }

    /**
     * Displays an alert showing the error message. App continues to run after closing the alert.
     */
    public void showNonFatalError(String message) {
        createAlert(message).show();
    }

    /**
     * Displays an alert showing the error message. Closes the application after alert is closed.
     */
    public void showFatalError(String message) {
        createAlert(message).showAndWait();
        close();
    }

    private Alert createAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cody");
        alert.setHeaderText("An error has occurred!");
        alert.setContentText(message);
        return alert;
    }

    /**
     * Displays welcome message.
     */
    public void showWelcome() {
        showCodyResponse(WELCOME_MSG);
    }

    /**
     * Displays goodbye message.
     */
    public void showGoodbye() {
        showCodyResponse(GOODBYE_MSG);
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
