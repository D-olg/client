package com.coursework.client.utils;

public class ApiConfig {
    // Базовый URL
    public static final String BASE_URL = "http://localhost:8080/api";

    // Получение полного URL для регистрации
    public static String getRegisterUrl() {
        return BASE_URL + "/auth/register";
    }

    public static String getLoginUrl() {
        return BASE_URL + "/auth/login";
    }

    public static String getAllUsersUrl() {
        return BASE_URL + "/admin/users";
    }

}
