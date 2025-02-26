package com.debadatta.TrimTime.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.debadatta.TrimTime.model.Admin;
import com.debadatta.TrimTime.repo.AdminRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {

  @Autowired
  private AdminRepo adminRepo;
  @Autowired
  DynamoDBMapper dynamoDBMapper;

  public String createProfile(Admin admin) {
    return adminRepo.createProfile(admin);
  }

   public Optional<Admin> findByPhoneNumber(String phone_no) {
        List<Admin> admin = dynamoDBMapper.scan(Admin.class, new DynamoDBScanExpression());
        return admin.stream().filter(user -> user.getPhone_no().equals(phone_no)).findFirst();
    }

  public Admin updateProfile(String admin_id, Admin admin) {
    return adminRepo.updateProfile(admin_id, admin);

  }

  public String deleteProfile(String admin_id) {
    return adminRepo.deleteProfile(admin_id);

  }

}
