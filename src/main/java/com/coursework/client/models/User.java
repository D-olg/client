package com.coursework.client.models;

import java.time.LocalDateTime;

public class User {

    private Integer id;
    private String username;
    private String password;  // Добавляем поле password
    private String email;
    private Integer role;
    private LocalDateTime createdAt;  // Добавляем поле created_at
    private LocalDateTime lastLogin;
    private boolean onlineStatus;

    // Конструктор без пароля и created_at
    public User() {
    }

    // Конструктор с полями
    public User(Integer id, String username, String password, String email, Integer role, LocalDateTime createdAt, LocalDateTime lastLogin, boolean onlineStatus) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.onlineStatus = onlineStatus;
    }

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
