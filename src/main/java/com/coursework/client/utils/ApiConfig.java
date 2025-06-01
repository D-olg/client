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

    public static String getUpdateUserUrl() { return BASE_URL + "/admin/users"; }

    public static String getDeleteUserUrl() { return BASE_URL + "/admin/users"; }

    public static String getAddUserUrl(){ return BASE_URL + "/admin/users";}

    // URL для работы с компаниями
    public static String getAllCompaniesUrl() {
        return BASE_URL + "/admin/companies";
    }

    public static String getAddCompanyUrl() {
        return BASE_URL + "/admin/companies";
    }

    public static String getUpdateCompanyUrl() {
        return BASE_URL + "/admin/companies";
    }

    public static String getDeleteCompanyUrl() {
        return BASE_URL + "/admin/companies";
    }

    public static String getManualCalculationUrl() { return BASE_URL + "/user/calculate/manual"; }

    public static String getScenarioCalculationUrl() { return BASE_URL + "/user/calculate/scenario"; }

    public static String getValuationsUrl() { return BASE_URL + "/user/history/valuations"; }

    public static String getFinancialDataUrl(Integer scenarioId) { return BASE_URL + "/user/history/financial/" + scenarioId; }

    public static String getRatesUrl(Integer scenarioId) { return BASE_URL + "/user/history/rates/" + scenarioId; }

    public static String getExportUrl() { return BASE_URL + "/user/export"; }

    public static String getImportUrl() { return BASE_URL + "/user/import"; }

    public static String getCurrentUserUrl() { return BASE_URL + "/user/current"; }

    public static String getUpdateUserCompanyUrl(int id) {return BASE_URL + "/company/update/" + id; }

    public static String getCompanyForCurrentUserUrl() { return BASE_URL + "/company/current"; }

    public static String getStatisticsUrl() {
        return BASE_URL + "/statistics";
    }

    public static String getServerSettingsUrl() {
        return BASE_URL + "/admin/settings";
    }
}
