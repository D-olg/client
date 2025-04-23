package com.coursework.client.utils;

import com.coursework.client.dto.UserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.List;

public class ApiClient {
    public static boolean registerUser(String username, String password, String email) {
        try {
            // Используем ApiConfig для получения URL
            URL url = new URL(ApiConfig.getRegisterUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Обновленная строка JSON для передачи email
            String json = String.format("{\"username\":\"%s\", \"password\":\"%s\", \"email\":\"%s\"}", username, password, email);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            return responseCode == 200 || responseCode == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String loginUser(String username, String password) {
        try {
            URL url = new URL(ApiConfig.getLoginUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString(); // ← вернёт, например: "Login successful. Role: admin"
                }
            } else {
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<UserDTO> getAllUsers() {
        try {
            URL url = new URL(ApiConfig.getAllUsersUrl()); // например: http://localhost:8080/api/admin/users
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (InputStream inputStream = conn.getInputStream()) {
                    return objectMapper.readValue(inputStream, new TypeReference<List<UserDTO>>() {});
                }
            } else {
                System.out.println("Ошибка получения пользователей: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of(); // Возвращаем пустой список в случае ошибки
    }
}
