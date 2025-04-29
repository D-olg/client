package com.coursework.client.utils;

import com.coursework.client.models.User; // Используем User вместо UserDTO
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coursework.client.utils.JacksonConfig;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class ApiClient {

    private static ObjectMapper objectMapper = JacksonConfig.createObjectMapper();

    public static boolean registerUser(String username, String password, String email) {
        try {
            URL url = new URL(ApiConfig.getRegisterUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

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
                    return response.toString(); // Возвращаем, например: "Login successful. Role: admin"
                }
            } else {
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static List<User> getAllUsers(String username, String password) {
        try {
            URL url = new URL(ApiConfig.getAllUsersUrl()); // Например: http://localhost:8080/api/admin/users
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            System.out.println(url);

            // Создаем строку для заголовка Authorization в формате "Basic base64(username:password)"
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            System.out.println(encodedAuth);

            int responseCode = conn.getResponseCode();
            System.out.println("Получен код ответа от сервера: " + responseCode);

            if (responseCode == 200) {
                try (InputStream inputStream = conn.getInputStream()) {
                    List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});
                    System.out.println("Пользователи успешно получены: " + users.size() + " записей.");
                    return users;
                }
            } else {
                System.out.println("Ошибка получения пользователей: " + responseCode + " (username: " + username + ", password: " + password + ")");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при отправке запроса: " + e.getMessage());
            e.printStackTrace();
        }

        return List.of(); // Возвращаем пустой список в случае ошибки
    }

    public static boolean updateUser(User user, String username, String password) {
        try {
            URL url = new URL(ApiConfig.getUpdateUserUrl() + "/" + user.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            conn.setDoOutput(true);

            String json = objectMapper.writeValueAsString(user);
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

    public static boolean deleteUser(int userId, String username, String password) {
        try {
            URL url = new URL(ApiConfig.getDeleteUserUrl() + "/" + userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

            int responseCode = conn.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createUser(User user, String username, String password) {
        try {
            URL url = new URL(ApiConfig.getAddUserUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            conn.setDoOutput(true);


            String json = objectMapper.writeValueAsString(user);
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

}
