package com.debadatta.TrimTime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.dto.UserRegistrationRequest;
import com.debadatta.TrimTime.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CognitoService cognitoService;

    @Override
    public User registerUser(UserRegistrationRequest userRegistrationRequest) {
        String username = userRegistrationRequest.getUsername();
        String password = userRegistrationRequest.getPassword();
        String email = userRegistrationRequest.getEmail();
        // Register the user with Amazon Cognito
        return cognitoService.registerUser(username, email, password);
    }

    @Override
    public User loginUser(String username, String password) {
        return cognitoService.loginUser(username, password);
    }
}