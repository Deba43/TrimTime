package com.debadatta.TrimTime.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String name;
    private String mobileNumber;
    private String email;
    private String role; 
    private String otp;
}
