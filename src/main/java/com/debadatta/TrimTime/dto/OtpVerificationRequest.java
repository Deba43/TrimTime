package com.debadatta.TrimTime.dto;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String phone_no;
    private String otp;
}