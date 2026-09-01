package swell.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import swell.Swell;

/**
 * Provides the JavaFX GUI for Swell.
 */
public class Main extends Application {
    private final Swell swell = new Swell();

    /**
     * Starts the Swell GUI.
     *
     * @param stage main application window.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);

            stage.setTitle("Swell");
            stage.setMinHeight(360.0);
            stage.setMinWidth(420.0);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setSwell(swell);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
