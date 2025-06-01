package com.coursework.client.controllers;

import com.coursework.client.session.Session;
import com.coursework.client.utils.ApiClient;
import com.coursework.client.models.Company;
import com.coursework.client.models.User;
import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProfileController {

    @FXML public Text username;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button saveUserBtn;

    @FXML private TextField companyNameField;
    @FXML private TextArea companyDescriptionField;
    @FXML private Button saveCompanyBtn;

    private User currentUser;
    private Company currentCompany;

    @FXML
    public void initialize() {
        username.setText(Session.getUsername());
        loadUserAndCompanyData();
    }

    private void loadUserAndCompanyData() {
        // Получаем текущего пользователя
        this.currentUser = ApiClient.getCurrentUser(); // предполагается, что реализован
        this.currentCompany = ApiClient.getCompanyForCurrentUser(); // предполагается, что реализован

        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());
        }

        if (currentCompany != null) {
            companyNameField.setText(currentCompany.getName());
            companyDescriptionField.setText(currentCompany.getDescription());
        }
    }

    @FXML
    private void onSaveUser() {
        currentUser.setUsername(usernameField.getText());
        currentUser.setEmail(emailField.getText());

        String newPassword = passwordField.getText();
        if (!newPassword.isEmpty()) {
            currentUser.setPassword(newPassword);
        }

        boolean success = ApiClient.updateUser(currentUser);

        if (success) {
            showAlert("Пользователь обновлён", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Не удалось обновить пользователя", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onSaveCompany() {
        currentCompany.setName(companyNameField.getText());
        currentCompany.setDescription(companyDescriptionField.getText());

        boolean success = ApiClient.updateCompany(currentCompany);

        if (success) {
            showAlert("Компания обновлена", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Не удалось обновить компанию", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle("Результат");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FXML
    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/userActions.fxml");
    }
}
