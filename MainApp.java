package com.example.hangmangamejavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Objects;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Loads the FXML that defines the GUI
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/main.fxml"));

        // Create the Scene from the loaded FXML (900x600)
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        // Load the window icon image from resources
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hangmanimage.png")));

        // Set the icon on the Stage (main window)
        stage.getIcons().add(icon);

        // Set title for the window
        stage.setTitle("Hangman Game");

        // Attach the Scene to the Stage (window)
        stage.setScene(scene);

        // Display the show
        stage.show();
    }

    // Launches JavaFX and then calls start(Stage)
    public static void main(String[] args) {
        launch();
    }
}
