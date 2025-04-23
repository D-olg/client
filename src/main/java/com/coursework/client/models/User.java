package com.coursework.client.models;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class User {

    private IntegerProperty id;
    private StringProperty username;
    private StringProperty password;
    private StringProperty email;
    private ObjectProperty<LocalDateTime> createdAt;
    private IntegerProperty role;
    private ObjectProperty<LocalDateTime> lastLogin;
    private BooleanProperty onlineStatus;

    // Конструктор
    public User(int id, String username, String password, String email, LocalDateTime createdAt, int role, LocalDateTime lastLogin, boolean onlineStatus) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.role = new SimpleIntegerProperty(role);
        this.lastLogin = new SimpleObjectProperty<>(lastLogin);
        this.onlineStatus = new SimpleBooleanProperty(onlineStatus);
    }

    // Геттеры и сеттеры для каждого поля
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    public IntegerProperty roleProperty() {
        return role;
    }

    public ObjectProperty<LocalDateTime> lastLoginProperty() {
        return lastLogin;
    }

    public BooleanProperty onlineStatusProperty() {
        return onlineStatus;
    }

    // Методы для доступа к данным
    public int getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getEmail() {
        return email.get();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public int getRole() {
        return role.get();
    }

    public LocalDateTime getLastLogin() {
        return lastLogin.get();
    }

    public boolean isOnlineStatus() {
        return onlineStatus.get();
    }

    // Сеттеры
    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt.set(createdAt);
    }

    public void setRole(int role) {
        this.role.set(role);
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin.set(lastLogin);
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus.set(onlineStatus);
    }
}
