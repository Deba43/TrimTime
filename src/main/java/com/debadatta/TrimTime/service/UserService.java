package com.debadatta.TrimTime.service;

import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.dto.UserRegistrationRequest;

public interface UserService {
    User registerUser(UserRegistrationRequest userRegistrationRequest);

    User loginUser(String mobile_number, String otp);
}
