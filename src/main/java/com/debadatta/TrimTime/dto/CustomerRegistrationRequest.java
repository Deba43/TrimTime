package com.debadatta.TrimTime.dto;

import lombok.Data;

@Data
public class CustomerRegistrationRequest {
    private String name;
    private String age;
    private String mobileNumber;
    private String email;
    private String profilePictureUrl;
}
