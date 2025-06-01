package com.coursework.client.utils;

import com.coursework.client.dto.*;
import com.coursework.client.models.*;
import com.coursework.client.session.Session;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class ApiClient {

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = JacksonConfig.createObjectMapper();
    private static final String BASE_URL = "http://localhost:8080";

    private static String basicAuth(String username, String password) {
        String auth = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean registerUser(String username, String password, String email) {
        try {
            String json = String.format("{\"username\":\"%s\", \"password\":\"%s\", \"email\":\"%s\"}", username, password, email);

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(ApiConfig.getRegisterUrl())
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String loginUser(String username, String password) {
        try {
            String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(ApiConfig.getLoginUrl())
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return response.body().string();
                } else {
                    return "error";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static List<User> getAllUsers() {
        Request request = new Request.Builder()
                .url(ApiConfig.getAllUsersUrl())
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readValue(response.body().byteStream(), new TypeReference<List<User>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List.of();
    }

    public static boolean updateUser(User user) {
        try {
            String json = objectMapper.writeValueAsString(user);

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(ApiConfig.getUpdateUserUrl() + "/" + user.getId())
                    .put(body)
                    .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(int userId) {
        Request request = new Request.Builder()
                .url(ApiConfig.getDeleteUserUrl() + "/" + userId)
                .delete()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createUser(User user) {
        try {
            String json = objectMapper.writeValueAsString(user);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(ApiConfig.getAddUserUrl())
                    .post(body)
                    .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Company> getAllCompanies() {
        Request request = new Request.Builder()
                .url(ApiConfig.getAllCompaniesUrl())
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readValue(response.body().byteStream(), new TypeReference<List<Company>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public static boolean createCompany(Company company) {
        try {
            String json = objectMapper.writeValueAsString(company);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(ApiConfig.getAddCompanyUrl())
                    .post(body)
                    .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateCompany(Company company) {
        try {
            String json = objectMapper.writeValueAsString(company);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(ApiConfig.getUpdateCompanyUrl() + "/" + company.getId())
                    .put(body)
                    .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCompany(Integer companyId) {
        Request request = new Request.Builder()
                .url(ApiConfig.getDeleteCompanyUrl() + "/" + companyId)
                .delete()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getCurrentUser() {
        Request request = new Request.Builder()
                .url(ApiConfig.getCurrentUserUrl())
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readValue(response.body().byteStream(), User.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Company getCompanyForCurrentUser() {
        Request request = new Request.Builder()
                .url(ApiConfig.getCompanyForCurrentUserUrl())
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readValue(response.body().byteStream(), Company.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean updateUserCompany(Company company) {
        try {
            String json = objectMapper.writeValueAsString(company);

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(ApiConfig.getUpdateUserCompanyUrl(company.getId()))
                    .put(body)
                    .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Valuation sendManualCalculation(List<CombinedFinancialRow> combinedData, String description) throws IOException {
        ManualCalculationRequest requestData = new ManualCalculationRequest(description, combinedData); // создаём правильный объект

        String json = objectMapper.writeValueAsString(requestData);
        System.out.println("Запрос: " + json); // для отладки

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(ApiConfig.getManualCalculationUrl())
                .post(body)
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                Valuation valuation = objectMapper.readValue(responseBody, Valuation.class);
                System.out.println("Оценка успешно получена: " + valuation.getValuation());
                return valuation;
            } else {
                String errorBody = response.body() != null ? response.body().string() : "(пусто)";
                throw new IOException(response.code() + " " + response.message() + " — " + errorBody);
            }
        }
    }



    public static Valuation sendScenarioCalculation(List<CombinedFinancialRow> combinedData, String description,
                                                    double growthRate, double discountRate, int durationYears) throws IOException {
        ScenarioCalculationRequest requestData =
                new ScenarioCalculationRequest(combinedData, description, growthRate, discountRate, durationYears);

        String json = objectMapper.writeValueAsString(requestData);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        System.out.println("Запрос: " + json); // для отладки
        Request request = new Request.Builder()
                .url(ApiConfig.getScenarioCalculationUrl())
                .post(body)
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                Valuation valuation = objectMapper.readValue(responseBody, Valuation.class);
                System.out.println("Оценка успешно получена: " + valuation.getValuation());
                return valuation;
            } else {
                String errorBody = response.body() != null ? response.body().string() : "(пусто)";
                throw new IOException(response.code() + " " + response.message() + " — " + errorBody);
            }
        }
    }
    public List<ValuationDTO> getValuations() {
        String url = ApiConfig.getValuationsUrl();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return Collections.emptyList();
            return objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<FinancialData> getFinancialDataByScenario(Integer scenarioId) {
        String url = ApiConfig.getFinancialDataUrl(scenarioId);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return Collections.emptyList();
            return objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<DiscountRate> getDiscountRatesByScenario(Integer scenarioId) {
        String url = ApiConfig.getRatesUrl(scenarioId);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return Collections.emptyList();
            return objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public byte[] exportValuations(List<ValuationDTO> valuations, String format) throws IOException {
        // Преобразуем список DTO в JSON, чтобы передать его на сервер
        String json = objectMapper.writeValueAsString(valuations);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        HttpUrl url = HttpUrl.parse(ApiConfig.getExportUrl())
                .newBuilder()
                .addQueryParameter("format", format)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка при экспорте: " + response);
            }
            return response.body().bytes(); // Получаем файл в байтах
        }
    }

    public void importData(File file, String format) throws IOException {
        String url = ApiConfig.getImportUrl();

        MediaType mediaType = switch (format.toUpperCase()) {
            case "CSV" -> MediaType.parse("text/csv");
            case "XLSX" -> MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case "JSON" -> MediaType.parse("application/json");
            default -> throw new IllegalArgumentException("Неподдерживаемый формат: " + format);
        };

        RequestBody fileBody = RequestBody.create(file, mediaType);

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("format", format)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка при импорте: " + response);
            }
        }
    }

    public static StatisticsData getStatistics() {
        Request request = new Request.Builder()
                .url(ApiConfig.getStatisticsUrl())
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readValue(response.body().byteStream(), StatisticsData.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new StatisticsData(); // Return empty statistics if request fails
    }

    public static List<UserStats> getUsersStats() throws IOException {
        Request request = new Request.Builder()
                .url(ApiConfig.getStatisticsUrl() + "/users")
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get users statistics");
            }

            String responseBody = response.body().string();
            List<Map<String, Object>> userStatsList = objectMapper.readValue(responseBody,
                new TypeReference<List<Map<String, Object>>>() {});

            return userStatsList.stream()
                .map(map -> new UserStats(
                    (String) map.get("username"),
                    ((Number) map.get("calculationsCount")).intValue()
                ))
                .collect(Collectors.toList());
        }
    }

    public static Map<String, Object> getServerSettings() throws Exception {
        Request request = new Request.Builder()
                .url(ApiConfig.getServerSettingsUrl())
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new Exception("Failed to get server settings: " + response.code() + " - " + errorBody);
            }
            
            String json = response.body().string();
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        }
    }

    public static boolean testDatabaseConnection(String url, String username, String password) throws Exception {
        Map<String, String> testData = Map.of(
            "url", url,
            "username", username,
            "password", password
        );

        RequestBody body = RequestBody.create(
            objectMapper.writeValueAsString(testData),
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(ApiConfig.getServerSettingsUrl() + "/test-db")
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() && response.code() != 400) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new Exception("Database connection test failed: " + response.code() + " - " + errorBody);
            }
            return response.isSuccessful();
        }
    }

    public static boolean updateServerSettings(Map<String, Object> settings) throws Exception {
        RequestBody body = RequestBody.create(
            objectMapper.writeValueAsString(settings),
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(ApiConfig.getServerSettingsUrl())
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new Exception("Failed to update server settings: " + response.code() + " - " + errorBody);
            }
            return true;
        }
    }

    public static boolean resetServerSettings() throws Exception {
        Request request = new Request.Builder()
                .url(ApiConfig.getServerSettingsUrl() + "/reset")
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new Exception("Failed to reset server settings: " + response.code() + " - " + errorBody);
            }
            return true;
        }
    }

    public static List<String> getSpringLogs(String level) throws IOException {
        String url = BASE_URL + "/api/logs/spring" + (level != null ? "?level=" + level : "");
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readValue(
                    response.body().byteStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                );
            }
        }
        return List.of();
    }

    public static List<ImportExportLog> getImportExportLogs() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/logs/import-export")
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readValue(
                    response.body().byteStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ImportExportLog.class)
                );
            }
        }
        return List.of();
    }

    public static byte[] downloadSpringLog() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/logs/spring/download")
                .get()
                .addHeader("Authorization", basicAuth(Session.getUsername(), Session.getPassword()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().bytes();
            }
        }
        return new byte[0];
    }

}
