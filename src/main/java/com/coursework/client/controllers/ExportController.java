package com.coursework.client.controllers;

import com.coursework.client.dto.ValuationDTO;
import com.coursework.client.utils.ApiClient;
import com.coursework.client.utils.SceneNavigator;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ExportController {

    @FXML public Button chooseFolderButton;
    @FXML private TableView<ValuationDTO> valuationTable;
    @FXML private TableColumn<ValuationDTO, Boolean> selectColumn;
    @FXML private TableColumn<ValuationDTO, String> companyNameColumn;
    @FXML private TableColumn<ValuationDTO, String> dateColumn;
    @FXML private TableColumn<ValuationDTO, String> valueColumn;
    @FXML private ComboBox<String> formatComboBox;
    @FXML private Button exportSingleFileButton;
    @FXML private Label folderPathLabel;  // обязательно подключи, если ещё не подключён
    @FXML private TextField fileNameTextField;

    private File selectedDirectory;
    private final ObservableList<ValuationDTO> valuations = FXCollections.observableArrayList();
    private final ApiClient apiClient = new ApiClient();

    @FXML
    public void initialize() {
        valuationTable.setEditable(true);
        selectColumn.setEditable(true);

        formatComboBox.setValue("CSV");

        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        selectColumn.setCellFactory(col -> {
            CheckBoxTableCell<ValuationDTO, Boolean> cell = new CheckBoxTableCell<>() {
                private final CheckBox checkBox = new CheckBox();

                {
                    checkBox.setOnAction(event -> {
                        if (getTableRow().getItem() != null) {
                            ValuationDTO item = getTableRow().getItem();
                            item.setSelected(checkBox.isSelected());
                            commitEdit(checkBox.isSelected()); // завершаем редактирование
                        }
                    });
                    setGraphic(checkBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && getTableRow().getItem() != null) {
                        checkBox.setSelected(getTableRow().getItem().isSelected());
                        setGraphic(checkBox);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return cell;
        });

        companyNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getScenario().getCompanyName()));

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreatedAtFormatted()));

        valueColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValuationFormatted()));

        valuationTable.setItems(valuations);
        loadValuations();
    }

    private void loadValuations() {
        List<ValuationDTO> list = apiClient.getValuations();
        valuations.setAll(list);  // Просто добавляем объекты ValuationDTO
    }

    @FXML
    private void onExportSingleFileClicked() {
        List<ValuationDTO> selected = valuations.stream()
                .filter(ValuationDTO::isSelected)
                .collect(Collectors.toList());

        if (selected.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Выберите хотя бы один расчет.");
            return;
        }

        if (selectedDirectory == null) {
            showAlert(Alert.AlertType.WARNING, "Сначала выберите папку для сохранения.");
            return;
        }

        // Получаем название файла из текстового поля
        String customFileName = fileNameTextField.getText().trim();
        if (customFileName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Введите название файла.");
            return;
        }

        String format = formatComboBox.getValue(); // CSV, XLSX, JSON, PDF
        String fileName = customFileName + "." + format.toLowerCase(); // Используем пользовательское имя
        File outputFile = new File(selectedDirectory, fileName);

        try {
            byte[] fileBytes = apiClient.exportValuations(selected, format);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(fileBytes);
            }
            showAlert(Alert.AlertType.INFORMATION, "Файл успешно экспортирован:\n" + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка при экспорте файла.");
        }
    }

    @FXML
    private void onChooseFolderClicked() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Выберите папку для экспорта");

        Stage stage = (Stage) chooseFolderButton.getScene().getWindow();
        File dir = chooser.showDialog(stage);

        if (dir != null) {
            selectedDirectory = dir;
            folderPathLabel.setText(dir.getAbsolutePath());
        }
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
