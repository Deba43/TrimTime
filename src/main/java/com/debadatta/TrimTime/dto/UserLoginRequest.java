package com.debadatta.TrimTime.dto;

import jakarta.validation.constraints.NotNull;

public class UserLoginRequest {
    @NotNull(message = "Username is required")
    private String username;

    public String getUsername() {
        return username;
    }

}
