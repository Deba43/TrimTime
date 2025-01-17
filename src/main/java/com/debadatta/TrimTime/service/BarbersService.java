package com.debadatta.TrimTime.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.debadatta.TrimTime.dto.BarberRegistrationRequest;
import com.debadatta.TrimTime.dto.CustomerRegistrationRequest;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.repo.BarbersRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BarbersService {

  @Autowired
  private BarbersRepo barbersRepo;
  @Autowired
  final private DynamoDBMapper dynamoDBMapper;

  private Map<String, String> otpStorage = new HashMap<>();

  public String createProfile(Barbers barbers) {
    return barbersRepo.createProfile(barbers);
  }

  public String generateAndSendOTP(String mobileNumber) {

    String otp = String.format("%06d", new Random().nextInt(999999));
    otpStorage.put(mobileNumber, otp);

    AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
        .withRegion("ap-east-1")
        .build();
    PublishRequest publishRequest = new PublishRequest()
        .withMessage("Your OTP is: " + otp)
        .withPhoneNumber(mobileNumber);
    snsClient.publish(publishRequest);

    System.out.println("Sending OTP: " + otp + " to mobile number: " + mobileNumber);
    return otp;
  }

  public Barbers verifyOTPAndRegister(String mobileNumber, String otp, BarberRegistrationRequest request) {
    String storedOtp = otpStorage.get(mobileNumber);

    if (storedOtp == null || !storedOtp.equals(otp)) {
      throw new IllegalArgumentException("Invalid or expired OTP");
    }

    otpStorage.remove(mobileNumber);

    Barbers barber = new Barbers();
    barber.setName(request.getName());
    barber.setBarberShopName(request.getBarberShopName());
    barber.setLocation(request.getLocation());
    barber.setMobileNumber(request.getMobileNumber());
    barber.setEmail(request.getEmail());
    barber.setProfilePictureUrl(request.getProfilePictureUrl());

    dynamoDBMapper.save(barber);

    return barber;
  }

  public String deleteProfile(String barber_id) {
    return barbersRepo.deleteProfile(barber_id);

  }

  public Barbers updateProfile(String barber_id, Barbers barbers) {
    return barbersRepo.updateProfile(barber_id, barbers);
  }

}
