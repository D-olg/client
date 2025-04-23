package com.coursework.client.controllers;

import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;

public class StartController {

    @FXML
    private void onLoginClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/login.fxml");
    }

    @FXML
    private void onRegisterClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/register.fxml");
    }

    @FXML
    private void onGuestClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/guest.fxml");
    }

    @FXML
    private void onExitClicked() {
        System.exit(0); // Завершение приложения
    }

    private void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
