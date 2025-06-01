package com.coursework.client.controllers;

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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;

public class UserCRUDController {

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> lastLoginColumn;
    @FXML
    private TableColumn<User, Boolean> onlineStatusColumn;
    @FXML
    private TableColumn<User, Void> actionsColumn;

    @FXML
    private void initialize() {
        usersTable.setEditable(true);

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setUsername(event.getNewValue());
            ApiClient.updateUser(user);
        });

        // Email — не редактируемый
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        emailColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String email, boolean empty) {
                super.updateItem(email, empty);
                setText(empty ? null : email);
            }
        });

        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole() == 1 ? "Admin" : "User"));
        roleColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Admin", "User"));
        roleColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setRole(event.getNewValue().equals("Admin") ? 1 : 0);
            ApiClient.updateUser(user);
        });

        lastLoginColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastLogin() != null ?
                        cellData.getValue().getLastLogin().toString() : "N/A"));

        // Online Status — цветовой индикатор
        onlineStatusColumn.setCellFactory(column -> new TableCell<>() {
            private final Circle statusIndicator = new Circle(6);

            @Override
            protected void updateItem(Boolean online, boolean empty) {
                super.updateItem(online, empty);
                if (empty || online == null) {
                    setGraphic(null);
                } else {
                    statusIndicator.setFill(online ? Color.LIMEGREEN : Color.CRIMSON);
                    setGraphic(statusIndicator);
                }
            }
        });
        onlineStatusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isOnlineStatus()));

        // Только кнопка Delete
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final HBox container = new HBox(10, deleteButton);

            {
                deleteButton.setOnAction(e -> {
                    User item = getTableView().getItems().get(getIndex());
                    boolean success = ApiClient.deleteUser(item.getId());
                    if (success) {
                        getTableView().getItems().remove(item);
                    }
                });
                deleteButton.getStyleClass().add("danger-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        loadUsers();
    }

    @FXML
    private void onReloadClicked() {
        loadUsers();
    }

    private void loadUsers() {
        List<User> users = ApiClient.getAllUsers();
        usersTable.getItems().setAll(users);
    }

    @FXML
    private void onAddUserClicked() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Добавить пользователя");
        dialog.setHeaderText("Введите данные нового пользователя");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/coursework/css/adminStyles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-dialog");

        Platform.runLater(() -> {
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/coursework/icons/symbol.png")));
        });

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Имя пользователя");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Пользователь", "Администратор");
        roleComboBox.setValue("Пользователь");

        grid.add(new Label("Имя пользователя:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Роль:"), 0, 3);
        grid.add(roleComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Включаем/выключаем кнопку добавления в зависимости от валидности полей
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Валидация полей
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValid = !newValue.trim().isEmpty() 
                            && !passwordField.getText().trim().isEmpty() 
                            && !emailField.getText().trim().isEmpty();
            addButton.setDisable(!isValid);
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValid = !newValue.trim().isEmpty() 
                            && !usernameField.getText().trim().isEmpty() 
                            && !emailField.getText().trim().isEmpty();
            addButton.setDisable(!isValid);
        });

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValid = !newValue.trim().isEmpty() 
                            && !usernameField.getText().trim().isEmpty() 
                            && !passwordField.getText().trim().isEmpty();
            addButton.setDisable(!isValid);
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    User newUser = new User();
                    newUser.setUsername(usernameField.getText().trim());
                    newUser.setPassword(passwordField.getText().trim());
                    newUser.setEmail(emailField.getText().trim());
                    newUser.setRole(roleComboBox.getValue().equals("Администратор") ? 1 : 0);
                    return newUser;
                } catch (Exception e) {
                    showAlert("Ошибка", "Произошла ошибка при создании пользователя");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            boolean success = ApiClient.createUser(user);
            if (success) {
                loadUsers();
            } else {
                showAlert("Ошибка", "Не удалось создать пользователя");
            }
        });
    }

    @FXML
    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/adminActions.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
