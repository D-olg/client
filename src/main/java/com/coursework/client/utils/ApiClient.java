package com.coursework.client.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

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

    public static boolean loginUser(String username, String password) {
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
            return responseCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
