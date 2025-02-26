package com.debadatta.TrimTime.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.debadatta.TrimTime.dto.AdminDto;
import com.debadatta.TrimTime.dto.BarbersDto;
import com.debadatta.TrimTime.dto.CustomersDto;
import com.debadatta.TrimTime.dto.OtpVerificationRequest;
import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.model.Admin;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.service.AdminService;
import com.debadatta.TrimTime.service.BarbersService;
import com.debadatta.TrimTime.service.CognitoService;
import com.debadatta.TrimTime.service.CustomersService;
import com.debadatta.TrimTime.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AdminService adminService;
    @Autowired
    BarbersService barbersService;
    @Autowired
    CustomersService customersService;
    @Autowired
    UserService userService;
    @Autowired
    CognitoService cognitoService;
    @Autowired
    static Environment environment;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        String phone_no = request.getPhone_no();

        Optional<Admin> adminOptional = adminService.findByPhoneNumber(phone_no);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            ensureCognitoUserExists(phone_no, "ADMIN");
            userService.generateAndSendOTP(phone_no, "ADMIN");
            return ResponseEntity.ok("OTP sent to ADMIN");
        }

        Optional<Barbers> barberOptional = barbersService.findByPhoneNumber(phone_no);
        if (barberOptional.isPresent()) {
            Barbers barber = barberOptional.get();
            ensureCognitoUserExists(phone_no, "BARBER");
            userService.generateAndSendOTP(phone_no, "BARBER");
            return ResponseEntity.ok("OTP sent to BARBER");
        }

        // Check Customer
        Optional<Customers> customerOptional = customersService.findByPhoneNumber(phone_no);
        if (customerOptional.isPresent()) {
            Customers customer = customerOptional.get();
            ensureCognitoUserExists(phone_no, "CUSTOMER");
            userService.generateAndSendOTP(phone_no, "CUSTOMER");
            return ResponseEntity.ok("OTP sent to CUSTOMER");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }

    private void ensureCognitoUserExists(String phoneNo, String role) {
        if (!cognitoService.userExists(phoneNo)) {
            cognitoService.createUser(phoneNo, role);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        try {
            String phoneNo = request.getPhone_no();
            String otp = request.getOtp();

            String role = userService.verifyOtpAndGetRole(phoneNo, otp);

            User cognitoUser = cognitoService.loginUser(phoneNo, otp);

            String userId = cognitoService.getUserIdFromToken(cognitoUser.getId());

            userService.saveUser(userId, phoneNo, role);

            return ResponseEntity.ok(cognitoUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminDto adminDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            userService.saveAdmin(adminDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/signup/barber")
    public ResponseEntity<?> registerBarber(@Valid @RequestBody BarbersDto barbersDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            userService.saveBarber(barbersDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Barber registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomersDto customersDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            userService.saveCustomer(customersDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}
