package com.example.backend.model.entity;

public class ResetPasswordRequest {
    private String token;
    private String newPassword;

    // Constructor
    public ResetPasswordRequest() {}

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}


