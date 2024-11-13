package com.debadatta.TrimTime.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.repo.CustomersRepo;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    private final DynamoDBMapper dynamoDBMapper;
    private final CustomersRepo customersRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AmazonSNS snsClient;

    @Autowired
    public AuthenticationService(
            DynamoDBMapper dynamoDBMapper,
            CustomersRepo customersRepo,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            AmazonSNS snsClient
    ) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.customersRepo = customersRepo;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.snsClient = snsClient;
    }

    public Customers signup(Customers input) {
        Optional<Customers> existingCustomer = customersRepo.findByMobileNumber(input.getMobileNumber());
        if (existingCustomer.isPresent()) {
            throw new IllegalArgumentException("Mobile number already registered");
        }

        // Encrypt the password and save the customer
        Customers customer = new Customers()
                .setName(input.getName())
                .setMobileNumber(input.getMobileNumber())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        dynamoDBMapper.save(customer);

        // Send OTP after signup
        sendOtp(customer.getMobileNumber());

        return customer; // Return the saved customer
    }

    public Customers authenticate(Customers input, String otp) {
        // Verify the OTP (implement your own logic to store and validate OTP)
        if (!verifyOtp(input.getMobileNumber(), otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // Authenticate user using mobile number and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getMobileNumber(),
                        input.getPassword()
                )
        );

        return customersRepo.findByMobileNumber(input.getMobileNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void sendOtp(String mobileNumber) {
        // Generate a random OTP
        String otp = String.valueOf(new Random().nextInt(999999)); // 6-digit OTP
        String message = "Your OTP is: " + otp;

        // Store OTP associated with mobile number in your database or cache for verification
        storeOtp(mobileNumber, otp);

        // Send OTP via SMS using SNS
        PublishRequest publishRequest = new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(mobileNumber);
        PublishResult publishResult = snsClient.publish(publishRequest);
    }

    private void storeOtp(String mobileNumber, String otp) {
        // Implement logic to store the OTP (e.g., in a database or in-memory cache)
        // This should include an expiration time for the OTP
    }

    private boolean verifyOtp(String mobileNumber, String otp) {
        // Implement logic to retrieve and verify the OTP stored previously
        // Check if the OTP matches and is not expired
        return true; // Replace with actual verification logic
    }
}
