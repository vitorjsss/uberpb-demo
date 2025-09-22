package com.uberpb.session;

import java.time.LocalDateTime;

public class SessionData {
    private int userId;
    private String email;
    private LocalDateTime loginTime;
    private LocalDateTime lastActivity;

    public SessionData() {
    }

    public SessionData(int userId, String email, LocalDateTime loginTime, LocalDateTime lastActivity) {
        this.userId = userId;
        this.email = email;
        this.loginTime = loginTime;
        this.lastActivity = lastActivity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }
}