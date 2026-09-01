package swell.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Represents one chat bubble with an avatar.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(image);
        setRoundAvatar(image);
    }

    /**
     * Returns a right-aligned dialog box for the user's input.
     *
     * @param text text to show.
     * @param image avatar image.
     * @return user dialog box.
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Returns a left-aligned dialog box for Swell's response.
     *
     * @param text text to show.
     * @param image avatar image.
     * @return Swell dialog box.
     */
    public static DialogBox getSwellDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        return dialogBox;
    }

    private void setRoundAvatar(Image image) {
        double sideLength = Math.min(image.getWidth(), image.getHeight());
        double xOffset = (image.getWidth() - sideLength) / 2;
        double yOffset = (image.getHeight() - sideLength) / 2;
        displayPicture.setViewport(new Rectangle2D(xOffset, yOffset, sideLength, sideLength));
        displayPicture.setClip(new Circle(32.0, 32.0, 32.0));
    }

    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        dialog.getStyleClass().add("reply-label");
    }
}
