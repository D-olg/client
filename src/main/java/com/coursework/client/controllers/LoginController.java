package com.coursework.client.controllers;

import com.coursework.client.session.Session;
import com.coursework.client.utils.ApiClient;
import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String response = ApiClient.loginUser(username, password);

        if (response.startsWith("Login successful")) {
            Session.setCredentials(username, password, response.contains("admin") ? "admin" : "user"); // сохраняем логин и пароль

            if (response.contains("admin")) {
                SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/com/coursework/client/adminActions.fxml");
            } else {
                SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/com/coursework/client/userActions.fxml");
            }
        } else {
            statusLabel.setText("Неверный логин или пароль.");
        }
    }


    @FXML
    private void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/start.fxml");
    }
}
