package com.coursework.client.controllers;

import com.coursework.client.utils.ApiClient;
import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ImportController {

    @FXML private ComboBox<String> formatComboBox;
    @FXML private Button chooseFileButton;
    @FXML private Label filePathLabel;
    @FXML private Button importButton;

    private File selectedFile;
    private final ApiClient apiClient = new ApiClient();

    @FXML
    public void initialize() {
        formatComboBox.setValue("CSV");

        chooseFileButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите файл для импорта");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV", "*.csv"),
                    new FileChooser.ExtensionFilter("XLSX", "*.xlsx"),
                    new FileChooser.ExtensionFilter("JSON", "*.json")
            );

            Stage stage = (Stage) chooseFileButton.getScene().getWindow();
            selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                filePathLabel.setText(selectedFile.getAbsolutePath());
            }
        });

        importButton.setOnAction(event -> {
            if (selectedFile == null) {
                showAlert(Alert.AlertType.WARNING, "Пожалуйста, выберите файл.");
                return;
            }

            String format = formatComboBox.getValue();
            try {
                apiClient.importData(selectedFile, format);
                showAlert(Alert.AlertType.INFORMATION, "Импорт и расчет завершены успешно.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Ошибка при импорте данных.");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/userActions.fxml");
    }
}
