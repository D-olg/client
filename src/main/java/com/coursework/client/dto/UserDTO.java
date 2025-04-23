package com.coursework.client.dto;

public class UserDTO {
    private int id;
    private String username;
    private String email;
    private int role;

    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}