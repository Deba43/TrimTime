package com.debadatta.TrimTime.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.repo.CustomersRepo;

@Service
public class AuthenticationService {

    private final CognitoService cognitoService;
    @Autowired
    private CustomersRepo customersRepo;

    public AuthenticationService(CognitoService cognitoService) {
        this.cognitoService = cognitoService;
    }

    public Customers authenticate(String mobileNumber, String otp) {
        User user = cognitoService.loginUser(mobileNumber, otp);

        // Create a Customers object from the User information
        return customersRepo.findByMobileNumber(user.getMobile_number())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String refreshAccessToken(String refreshToken) {
        return cognitoService.refreshToken(refreshToken);
    }

}
