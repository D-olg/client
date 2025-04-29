package com.coursework.client.controllers;

import com.coursework.client.models.User;
import com.coursework.client.session.Session;
import com.coursework.client.utils.ApiClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class UserController {

    @FXML
    private TableView<User> usersTable; // Используем User вместо UserDTO
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> lastLoginColumn; // Новый столбец для отображения последнего входа
    @FXML
    private TableColumn<User, String> onlineStatusColumn; // Новый столбец для отображения статуса онлайн
    @FXML
    private TableColumn<User, Void> actionsColumn;


    @FXML
    private void initialize() {
        usersTable.setEditable(true); // Включаем редактирование таблицы

        // ID — не редактируемый, просто отображаем
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        // Username — редактируемый
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setUsername(event.getNewValue());
            ApiClient.updateUser(user, Session.getUsername(), Session.getPassword());
        });

        // Email — редактируемый
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setEmail(event.getNewValue());
            ApiClient.updateUser(user, Session.getUsername(), Session.getPassword());
        });

        // Role — редактируемый
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole() == 1 ? "Admin" : "User"));
        roleColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Admin", "User"));
        roleColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setRole(event.getNewValue().equals("Admin") ? 1 : 0);  // Предполагаем, что 1 - Admin, 2 - User
            ApiClient.updateUser(user, Session.getUsername(), Session.getPassword());
        });

        // Last Login — не редактируемый, просто отображаем
        lastLoginColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getLastLogin() != null ? cellData.getValue().getLastLogin().toString() : "N/A"));

        // Online Status — не редактируемый, просто отображаем
        onlineStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isOnlineStatus() ? "Online" : "Offline"));

        // Действия: Edit и Delete
        actionsColumn.setCellFactory(col -> new TableCell<User, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox container = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(e -> {
                    // Включаем редактирование таблицы
                    usersTable.setEditable(true);
                    System.out.println("Table is now editable.");
                });

                deleteButton.setOnAction(e -> {
                    User item = getTableView().getItems().get(getIndex());
                    boolean success = ApiClient.deleteUser(item.getId(), Session.getUsername(), Session.getPassword());
                    if (success) {
                        getTableView().getItems().remove(item);
                    }
                });

                editButton.getStyleClass().add("action-button");
                deleteButton.getStyleClass().add("danger-button");
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

        loadUsers();
    }


    @FXML
    private void onReloadClicked() {
        loadUsers();
    }

    private void loadUsers() {
        String username = Session.getUsername();
        String password = Session.getPassword();
        // Получение всех пользователей через API
        List<User> users = ApiClient.getAllUsers(username, password);
        usersTable.getItems().clear();
        usersTable.getItems().addAll(users);
    }

    // Пример обработки нажатия на кнопку редактирования
    private void onEditClicked(User user) {
        // Логика редактирования пользователя
        System.out.println("Edit user: " + user.getUsername());
    }

    @FXML
    private void onAddUserClicked() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter new user details");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/coursework/css/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-dialog");

        Platform.runLater(() -> {
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/coursework/icons/symbol.png")));
        });

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("User", "Admin");
        roleComboBox.setValue("User");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(roleComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                User newUser = new User();
                newUser.setUsername(usernameField.getText().trim());
                newUser.setPassword(passwordField.getText().trim());
                newUser.setEmail(emailField.getText().trim());
                newUser.setRole(roleComboBox.getValue().equalsIgnoreCase("Admin") ? 1 : 0);
                return newUser;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            boolean success = ApiClient.createUser(user, Session.getUsername(), Session.getPassword());
            if (success) {
                loadUsers();
            } else {
                showAlert("Error", "Failed to create user.");
            }
        });
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
