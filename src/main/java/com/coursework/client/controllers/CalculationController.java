package com.coursework.client.controllers;

import com.coursework.client.dto.CombinedFinancialRow;
import com.coursework.client.models.Valuation;
import com.coursework.client.utils.ApiClient;
import com.coursework.client.utils.SceneNavigator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class CalculationController {

    @FXML private TableView<CombinedFinancialRow> financialTable;
    @FXML private TableColumn<CombinedFinancialRow, Integer> yearColumn;
    @FXML private TableColumn<CombinedFinancialRow, Double> equityColumn;
    @FXML private TableColumn<CombinedFinancialRow, Double> netIncomeColumn;
    @FXML private TableColumn<CombinedFinancialRow, Double> discountRateColumn;

    @FXML private CheckBox useScenarioCheckBox;
    @FXML private VBox scenarioBox;
    @FXML private TextField scenarioDescriptionField;
    @FXML private TextField growthRateField;
    @FXML private TextField discountRateField;
    @FXML private TextField durationYearsField;

    @FXML private Button addRowButton;
    @FXML private Button deleteRowButton;
    private final ObservableList<CombinedFinancialRow> combinedData = FXCollections.observableArrayList();
    private final ObjectMapper mapper = new ObjectMapper();
    private final File tempDataFile = Paths.get(System.getProperty("user.home"), ".ebo-client", "tmp_combined_data.json").toFile();

    @FXML
    public void initialize() {
        yearColumn.setCellValueFactory(cell -> cell.getValue().yearProperty().asObject());
        equityColumn.setCellValueFactory(cell -> cell.getValue().equityProperty().asObject());
        netIncomeColumn.setCellValueFactory(cell -> cell.getValue().netIncomeProperty().asObject());
        discountRateColumn.setCellValueFactory(cell -> cell.getValue().discountRateProperty().asObject());

        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        equityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        netIncomeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        discountRateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        financialTable.setEditable(true);
        financialTable.setItems(combinedData);

        addRowButton.setOnAction(event -> combinedData.add(new CombinedFinancialRow()));

        loadTempData();
    }

    @FXML
    private void onScenarioToggle() {
        boolean selected = useScenarioCheckBox.isSelected();
        scenarioBox.setVisible(selected);
        scenarioBox.setManaged(selected);
    }

    @FXML
    private void onCalculate() {
        clearErrorStyles();

        if (!validateData()) return;

        saveTempData();

        try {
            Valuation valuation;
            if (useScenarioCheckBox.isSelected()) {
                valuation = ApiClient.sendScenarioCalculation(
                        combinedData,
                        scenarioDescriptionField.getText(),
                        Double.parseDouble(growthRateField.getText()),
                        Double.parseDouble(discountRateField.getText()),
                        Integer.parseInt(durationYearsField.getText())
                );
            } else {
                valuation = ApiClient.sendManualCalculation(combinedData, "Manual input");
            }

            showValuationDialog(valuation);

        } catch (IOException e) {
            showError("Ошибка при отправке данных: " + e.getMessage());
        }
    }
    private void showValuationDialog(Valuation valuation) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Результат оценки");
        alert.setHeaderText("Оценка стоимости компании выполнена");
        alert.setContentText(String.format(
                "Оценка: %.2f\nДата расчёта: %s",
                valuation.getValuation(),
                valuation.getCreatedAt().toLocalDate()
        ));
        alert.showAndWait();
    }


    private boolean validateData() {
        for (int i = 0; i < combinedData.size(); i++) {
            CombinedFinancialRow row = combinedData.get(i);
            if (row.getYear() < 1900 || row.getEquity() < 0 || row.getNetIncome() < 0 || row.getDiscountRate() <= 0) {
                showError("Некорректные данные в строке " + (i + 1));
                return false;
            }
        }

        if (useScenarioCheckBox.isSelected()) {
            if (scenarioDescriptionField.getText().isBlank()
                    || !isValidDouble(growthRateField.getText())
                    || !isValidDouble(discountRateField.getText())
                    || !isValidInteger(durationYearsField.getText())) {
                showError("Пожалуйста, исправьте ошибки в сценарии.");
                return false;
            }
        }

        return true;
    }

    private boolean isValidDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    public void onDeleteRow() {
        CombinedFinancialRow selectedRow = financialTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            combinedData.remove(selectedRow);
            saveTempData();  // Обновляем временные данные после удаления строки
        } else {
            showError("Пожалуйста, выберите строку для удаления.");
        }
    }

    private void saveTempData() {
        try {
            tempDataFile.getParentFile().mkdirs();
            mapper.writeValue(tempDataFile, combinedData);
        } catch (IOException e) {
            showError("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    private void loadTempData() {
        if (tempDataFile.exists()) {
            try {
                List<CombinedFinancialRow> rows = mapper.readValue(tempDataFile, new TypeReference<>() {});
                combinedData.setAll(rows);
            } catch (IOException e) {
                showError("Ошибка при загрузке данных: " + e.getMessage());
            }
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void clearErrorStyles() {
        for (TextField field : List.of(scenarioDescriptionField, growthRateField, discountRateField, durationYearsField)) {
            field.getStyleClass().remove("error");
        }
    }

    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/userActions.fxml");
    }
}
