package com.coursework.client.controllers;

import com.coursework.client.models.Company;
import com.coursework.client.models.User;
import com.coursework.client.utils.ApiClient;
import com.coursework.client.utils.SceneNavigator;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CompanyCRUDController {

    @FXML
    private TableView<Company> companyTable;
    @FXML
    private TableColumn<Company, Integer> idColumn;
    @FXML
    private TableColumn<Company, Integer> userIdColumn;
    @FXML
    private TableColumn<Company, String> nameColumn;
    @FXML
    private TableColumn<Company, String> descriptionColumn;
    @FXML
    private TableColumn<Company, String> createdAtColumn;
    @FXML
    private TableColumn<Company, Void> actionsColumn;

    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private void initialize() {
        companyTable.setEditable(true);

        // ID - только отображение
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        // userId - редактируемое целочисленное поле (можно заменить на TextFieldTableCell или ComboBox, если надо)
        userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
        userIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        userIdColumn.setOnEditCommit(event -> {
            Company company = event.getRowValue();
            company.setUserId(event.getNewValue());
            updateCompany(company);
        });

        // name - редактируемое текстовое поле
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Company company = event.getRowValue();
            company.setName(event.getNewValue());
            updateCompany(company);
        });

        // description - редактируемое текстовое поле
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(event -> {
            Company company = event.getRowValue();
            company.setDescription(event.getNewValue());
            updateCompany(company);
        });

        // created_at - только отображение
        createdAtColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCreatedAt() != null) {
                return new SimpleStringProperty(cellData.getValue().getCreatedAt().format(dtFormatter));
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        // Колонка действий: удаление
        actionsColumn.setCellFactory(col -> new TableCell<Company, Void>() {
            private final Button deleteButton = new Button("Удалить");
            private final HBox container = new HBox(deleteButton);

            {
                deleteButton.getStyleClass().add("danger-button");
                deleteButton.setOnAction(e -> {
                    Company company = getTableView().getItems().get(getIndex());
                    boolean success = ApiClient.deleteCompany(company.getId());
                    if (success) {
                        getTableView().getItems().remove(company);
                    } else {
                        showAlert("Ошибка", "Не удалось удалить компанию.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        loadCompanies();
    }

    @FXML
    private void onReloadClicked() {
        loadCompanies();
    }

    @FXML
    private void onAddCompanyClicked() {
        Dialog<Company> dialog = new Dialog<>();
        dialog.setTitle("Добавить компанию");
        dialog.setHeaderText("Введите данные новой компании");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/coursework/css/adminStyles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-dialog");

        // Добавляем иконку к диалогу
        Platform.runLater(() -> {
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/coursework/icons/symbol.png")));
        });

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField userIdField = new TextField();
        userIdField.setPromptText("ID пользователя");

        TextField nameField = new TextField();
        nameField.setPromptText("Название компании");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Описание");
        descriptionField.setPrefRowCount(3);
        descriptionField.setWrapText(true);

        // Добавляем ComboBox для выбора пользователя
        ComboBox<String> userComboBox = new ComboBox<>();
        List<User> users = ApiClient.getAllUsers();
        users.forEach(user -> userComboBox.getItems().add(user.getId() + " - " + user.getUsername()));
        if (!userComboBox.getItems().isEmpty()) {
            userComboBox.setValue(userComboBox.getItems().get(0));
        }

        grid.add(new Label("Пользователь:"), 0, 0);
        grid.add(userComboBox, 1, 0);
        grid.add(new Label("Название:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Описание:"), 0, 2);
        grid.add(descriptionField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Включаем/выключаем кнопку добавления в зависимости от валидности полей
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Валидация полей
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    Company newCompany = new Company();
                    String selectedUser = userComboBox.getValue();
                    int userId = Integer.parseInt(selectedUser.split(" - ")[0]);
                    newCompany.setUserId(userId);
                    newCompany.setName(nameField.getText().trim());
                    newCompany.setDescription(descriptionField.getText().trim());
                    return newCompany;
                } catch (Exception e) {
                    showAlert("Ошибка", "Произошла ошибка при создании компании");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(company -> {
            boolean success = ApiClient.createCompany(company);
            if (success) {
                loadCompanies();
            } else {
                showAlert("Ошибка", "Не удалось создать компанию");
            }
        });
    }

    @FXML
    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/adminActions.fxml");
    }

    private void loadCompanies() {
        List<Company> companies = ApiClient.getAllCompanies();
        companyTable.getItems().clear();
        companyTable.getItems().addAll(companies);
    }

    private void updateCompany(Company company) {
        boolean success = ApiClient.updateCompany(company);
        if (!success) {
            showAlert("Ошибка", "Не удалось обновить компанию");
            // Можно перезагрузить данные, чтобы откатить изменения в таблице
            loadCompanies();
        }
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
