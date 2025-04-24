package com.coursework.client.controllers;

import com.coursework.client.models.User;
import com.coursework.client.session.Session;
import com.coursework.client.utils.ApiClient;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class AdminController {

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
    private TableColumn<User, Button> actionsColumn;

    @FXML
    private void initialize() {

        // Привязка столбца ID
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        // Привязка столбца Username
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        // Привязка столбца Email
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        // Привязка столбца Role
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole() == 1 ? "Admin" : "User"));

        // Привязка столбца Last Login
        lastLoginColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastLogin() != null ? cellData.getValue().getLastLogin().toString() : "N/A"));

        // Привязка столбца Online Status
        onlineStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isOnlineStatus() ? "Online" : "Offline"));

        // Привязка столбца Actions
        actionsColumn.setCellValueFactory(cellData -> {
            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> onEditClicked(cellData.getValue()));
            return new SimpleObjectProperty<>(editButton);
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
}
