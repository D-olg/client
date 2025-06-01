package com.coursework.client.session;

public class Session {
    private static String username;
    private static String password;
    private static String role;

    public static void setCredentials(String user, String pass, String userRole) {
        username = user;
        password = pass;
        role = userRole;
    }
    public static void setCredentials(String user, String pass) {
        username = user;
        password = pass;
    }
    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isAdmin() {
        return "admin".equals(role);
    }

    public static void clear() {
        username = null;
        password = null;
        role = null;
    }
}
