package com.coursework.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {

    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
