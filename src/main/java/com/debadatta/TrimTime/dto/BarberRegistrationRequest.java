package com.debadatta.TrimTime.dto;

import lombok.Data;

@Data
public class BarberRegistrationRequest {
    private String name;
    private String BarberShopName;
    private String location;
    private String mobileNumber;
    private String email;
    private String profilePictureUrl;
}

