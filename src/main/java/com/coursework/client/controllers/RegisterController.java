package com.coursework.client.controllers;

import com.coursework.client.utils.ApiClient;
import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.coursework.client.session.Session;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;

    // Обработчик события нажатия на кнопку регистрации
    @FXML
    private void onRegisterClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        // Вызываем метод регистрации с email
        boolean success = ApiClient.registerUser(username, password, email);

        if (success) {
            showSuccess("Регистрация прошла успешно!");
            Session.setCredentials(username,password);
            SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(), "/com/coursework/client/userActions.fxml");
        } else {
            showError("Ошибка при регистрации");
        }
    }

    @FXML
    private void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/start.fxml");
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
