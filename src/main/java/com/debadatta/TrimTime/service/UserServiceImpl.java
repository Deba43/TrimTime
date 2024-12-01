package com.debadatta.TrimTime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.dto.UserRegistrationRequest;
import com.debadatta.TrimTime.repo.UserRepo;

// @Service
// public class UserServiceImpl implements UserService {
//     @Autowired
//     private UserRepo userRepo;

//     @Autowired
//     private CognitoService cognitoService;

//     @Override
//     public User registerUser(UserRegistrationRequest userRegistrationRequest) {
//         // Cognito integration for OTP verification and user creation
//         String mobileNumber = userRegistrationRequest.getMobileNumber();
//         String otp = userRegistrationRequest.getOtp();

//         if (!verifyOtp(mobileNumber, otp)) {
//             throw new RuntimeException("Invalid OTP");
//         }

//         return user;
//     }

//     @Override
//     public User loginUser(String mobile_number, String otp) {
//         return cognitoService.loginUser(mobile_number, otp);
//     }
// }