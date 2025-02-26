package com.debadatta.TrimTime.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, Long> otpExpiry = new HashMap<>();
    private static final int OTP_LENGTH = 6;
    private static final long OTP_EXPIRATION_TIME_MS = 5 * 60 * 1000; // 5 minutes

    private final AmazonSNS snsClient;

    @Value("${aws.sns.senderId}")
    private String senderId;

    @Value("${aws.sns.Region}")
    private String awsRegion;

    public OtpService() {
        this.snsClient = AmazonSNSClientBuilder.standard()
                .withRegion(awsRegion)
                .build();
    }

    public String generateOtp(String mobileNumber) {
        // Generate a 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Store OTP and its expiration time
        otpStorage.put(mobileNumber, otp);
        otpExpiry.put(mobileNumber, System.currentTimeMillis() + OTP_EXPIRATION_TIME_MS);

        // Send OTP via SMS using AWS SNS
        sendSms(mobileNumber, otp);

        return otp;
    }

    public boolean verifyOtp(String mobileNumber, String otp) {
        String storedOtp = otpStorage.get(mobileNumber);
        Long expiryTime = otpExpiry.get(mobileNumber);

        if (storedOtp == null || expiryTime == null) {
            return false;
        }

        if (System.currentTimeMillis() > expiryTime) {
            otpStorage.remove(mobileNumber);
            otpExpiry.remove(mobileNumber);
            return false; // OTP expired
        }

        boolean isValid = storedOtp.equals(otp);

        // Remove OTP after successful validation
        if (isValid) {
            otpStorage.remove(mobileNumber);
            otpExpiry.remove(mobileNumber);
        }

        return isValid;
    }

    private void sendSms(String mobileNumber, String otp) {
        String message = "Your OTP for login is: " + otp + ". This OTP is valid for 5 minutes.";
        try {
            PublishRequest request = new PublishRequest()
                    .withMessage(message)
                    .withPhoneNumber(mobileNumber)
                    .withMessageAttributes(Map.of(
                            "AWS.SNS.SMS.SenderID", new com.amazonaws.services.sns.model.MessageAttributeValue()
                                    .withStringValue(senderId)
                                    .withDataType("String"),
                            "AWS.SNS.SMS.SMSType", new com.amazonaws.services.sns.model.MessageAttributeValue()
                                    .withStringValue("Transactional") // Use "Promotional" for non-critical messages
                                    .withDataType("String")));

            PublishResult result = snsClient.publish(request);
            System.out.println("Message sent to " + mobileNumber + ". Message ID: " + result.getMessageId());
        } catch (Exception e) {
            System.err.println("Error sending OTP via SMS: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP via SMS", e);
        }
    }
}
