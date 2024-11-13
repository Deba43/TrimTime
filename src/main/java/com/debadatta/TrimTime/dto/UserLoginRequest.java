package com.debadatta.TrimTime.dto;

import jakarta.validation.constraints.NotNull;

public class UserLoginRequest {
    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Password is required")
    private String password;

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
