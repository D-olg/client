package com.coursework.client.controllers;

import com.coursework.client.dto.UserDTO;
import com.coursework.client.utils.ApiClient;
import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import com.coursework.client.models.User;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class AdminController {

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
    private TableColumn<User, Button> actionsColumn;

    @FXML
    private void initialize() {
        // Инициализация таблицы
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getRole() == 1 ? "Admin" : "User"
        ));

        loadUsers();
    }

    @FXML
    private void onReloadClicked() {
        loadUsers();
    }

    private void loadUsers() {
        List<UserDTO> users = ApiClient.getAllUsers();
        usersTable.getItems().clear();
        usersTable.getItems().addAll((User) users);
    }


    @FXML
    private void onEditUser(ActionEvent event) {
        // Логика для редактирования данных пользователя
    }

    @FXML
    private void onDeleteUser(ActionEvent event) {
        // Логика для удаления пользователя
    }
}
