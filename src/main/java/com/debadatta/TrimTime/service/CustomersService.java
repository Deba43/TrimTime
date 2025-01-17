package com.debadatta.TrimTime.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.debadatta.TrimTime.dto.CustomerRegistrationRequest;
import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.repo.AppointmentsRepo;
import com.debadatta.TrimTime.repo.CustomersRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomersService {

  @Autowired
  private CustomersRepo customersRepo;
  @Autowired
  private AppointmentsRepo appointmentsRepo;
  @Autowired
  final private DynamoDBMapper dynamoDBMapper;

  private Map<String, String> otpStorage = new HashMap<>();

  public String createProfile(Customers customers) {
    return customersRepo.createProfile(customers);
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

  public Customers verifyOTPAndRegister(String mobileNumber, String enteredOtp, CustomerRegistrationRequest request) {
    String storedOtp = otpStorage.get(mobileNumber);

    if (storedOtp == null || !storedOtp.equals(enteredOtp)) {
      throw new IllegalArgumentException("Invalid or expired OTP");
    }

    otpStorage.remove(mobileNumber);

    Customers customer = new Customers();
    customer.setName(request.getName());
    customer.setAge(request.getAge());
    customer.setMobileNumber(request.getMobileNumber());
    customer.setEmail(request.getEmail());
    customer.setProfilePictureUrl(request.getProfilePictureUrl());

    dynamoDBMapper.save(customer);

    return customer;
  }

  public String deleteProfile(String customer_id) {
    return customersRepo.deleteProfile(customer_id);

  }

  public List<Barbers> searchByBarbers(String name) {
    return customersRepo.findBarbersByName(name);
  }

  public List<Barbers> searchByLocation(String location) {
    return customersRepo.findBarbersByLocation(location);
  }

  public String bookAppointment(Appointments appointment) {
    return appointmentsRepo.bookAppointment(appointment);
  }

  public String cancelAppointment(Appointments appointment) {
    return appointmentsRepo.cancelAppointment(appointment);
  }

  public Customers updateProfile(String customer_id, Customers customers) {
    return customersRepo.updateProfile(customer_id, customers);
  }

}
