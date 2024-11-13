package com.debadatta.TrimTime.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {

    private int id;

    @NotNull(message = "Username is required")
    @Size(min = 5, max = 50)
    private String username;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    private String accessToken;

}
