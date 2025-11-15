package com.clarity.app;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * SessionManager - Manages user session data across scenes
 *
 * Features:
 * - Store/retrieve user information
 * - Pass data between scenes
 * - Track login state
 * - Session timeout handling
 */
public class SessionManager {

    private static SessionManager instance;

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    private User currentUser;
    private LocalDateTime loginTime;
    private Map<String, Object> sessionData;

    private SessionManager() {
        this.sessionData = new HashMap<>();
    }

    public void login(String userId, String username, String email) {
        this.currentUser = new User(userId, username, email);
        this.loginTime = LocalDateTime.now();
        System.out.println("User logged in: " + username);
    }

    public void logout() {
        System.out.println("User logged out: " +
                (currentUser != null ? currentUser.getUsername() : "Unknown"));
        this.currentUser = null;
        this.loginTime = null;
        this.sessionData.clear();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void put(String key, Object value) {
        sessionData.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) sessionData.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Object value = sessionData.get(key);
        return value != null ? (T) value : defaultValue;
    }

    public boolean contains(String key) {
        return sessionData.containsKey(key);
    }

    public void remove(String key) {
        sessionData.remove(key);
    }

    public void clear() {
        sessionData.clear();
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public static class User {
        private String userId;
        private String username;
        private String email;
        private String displayName;
        private String avatarPath;

        public User(String userId, String username, String email) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.displayName = username;
        }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }

        public String getAvatarPath() { return avatarPath; }
        public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }
    }
}