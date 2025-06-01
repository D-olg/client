package com.coursework.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/coursework/icons/symbol.png")));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/coursework/client/start.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("ValuateEBO");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(null);
        primaryStage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
