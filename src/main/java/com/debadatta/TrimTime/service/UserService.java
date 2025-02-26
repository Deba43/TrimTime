package com.debadatta.TrimTime.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.debadatta.TrimTime.dto.AdminDto;
import com.debadatta.TrimTime.dto.BarbersDto;
import com.debadatta.TrimTime.dto.CustomersDto;
import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.model.Admin;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private final DynamoDBMapper dynamoDBMapper;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public UserService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper,
            DynamoDBMapper dynamoDBMapper, PasswordEncoder passwordEncoder) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.dynamoDBMapper = dynamoDBMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveAdmin(AdminDto adminDTO) {
        try {
            Admin admin = new Admin();
            admin.setName(adminDTO.getName());
            admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
            admin.setRole(adminDTO.getRole());
            admin.setEmail(adminDTO.getEmail());
            admin.setPhone_no(adminDTO.getPhone_no());
            dynamoDBMapper.save(admin);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not create Admin: " + e.getMessage());
        }
    }

    public void saveBarber(BarbersDto barbersDTO) {
        try {
            Barbers bar = new Barbers();
            bar.setName(barbersDTO.getName());
            bar.setBarberShopName(barbersDTO.getBarberShopName());
            bar.setLocation(barbersDTO.getLocation());
            bar.setExperience(barbersDTO.getExperience());
            bar.setPassword(passwordEncoder.encode(barbersDTO.getPassword()));
            bar.setRole(barbersDTO.getRole());
            bar.setEmail(barbersDTO.getEmail());
            bar.setPhone_no(barbersDTO.getPhone_no());
            bar.setBio(barbersDTO.getBio());

            dynamoDBMapper.save(bar);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not create Barber: " + e.getMessage());
        }
    }

    public void saveCustomer(CustomersDto customersDTO) {
        try {
            Customers cus = new Customers();
            cus.setName(customersDTO.getName());
            cus.setPassword(passwordEncoder.encode(customersDTO.getPassword()));
            cus.setEmail(customersDTO.getEmail());
            cus.setAge(customersDTO.getAge());
            cus.setPhone_no(customersDTO.getPhone_no());
            dynamoDBMapper.save(cus);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not create Customer: " + e.getMessage());
        }
    }

    public String generateAndSendOTP(String mobileNumber, String role) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        Map<String, String> otpData = new HashMap<>();
        otpData.put("otp", otp);
        otpData.put("role", role);

        try {
            // Serialize the Map to a JSON string
            String otpDataJson = objectMapper.writeValueAsString(otpData);
            redisTemplate.opsForValue().set(mobileNumber, otpDataJson, 5, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize OTP data", e);
        }

        // Send OTP via SMS (using Amazon SNS)
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withRegion("ap-south-1")
                .build();
        PublishRequest publishRequest = new PublishRequest()
                .withMessage("Your OTP is: " + otp)
                .withPhoneNumber(mobileNumber);
        snsClient.publish(publishRequest);

        System.out.println("Sending OTP: " + otp + " to mobile number: " + mobileNumber);
        return otp;
    }

    public String verifyOtpAndGetRole(String phoneNo, String otp) {
        String otpDataJson = redisTemplate.opsForValue().get(phoneNo);
        if (otpDataJson == null) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        try {
            // Deserialize the JSON string to a Map
            Map<String, String> otpData = objectMapper.readValue(otpDataJson, Map.class);
            if (!otp.equals(otpData.get("otp"))) {
                throw new IllegalArgumentException("Invalid or expired OTP");
            }
            String role = otpData.get("role");
            redisTemplate.delete(phoneNo);
            return role;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize OTP data", e);
        }
    }

    public void saveUser(String userId, String phoneNo, String role) {
        User user = new User();
        user.setId(userId);
        user.setPhone_no(phoneNo);
        user.setRole(role);
        dynamoDBMapper.save(user);
    }

}
