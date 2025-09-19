package cody.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog in the chat window, could be from either Cody or the user.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            URL fxmlUrl = getClass().getResource("/view/DialogBox.fxml");
            assert fxmlUrl != null;

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            Ui.showFatalError(e.getMessage());
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.BOTTOM_LEFT);
        getStyleClass().add("cody-dialog");
    }

    /**
     * Creates a dialog box for the user's command.
     *
     * @param text The text to display.
     * @param img The image to display.
     * @return A dialog box for the user.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Creates a dialog box for Cody's response.
     *
     * @param text The text to display.
     * @param img The image to display.
     * @return A dialog box for Cody.
     */
    public static DialogBox getCodyDialog(String text, Image img) {
        DialogBox dialog = new DialogBox(text, img);
        dialog.flip();
        return dialog;
    }

}
