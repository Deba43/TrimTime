package com.debadatta.TrimTime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.model.Admin;
import com.debadatta.TrimTime.repo.AdminRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {

  @Autowired
  private AdminRepo adminRepo;

  public String createProfile(Admin admin) {
    return adminRepo.createProfile(admin);
  }

  public Admin updateProfile(String admin_id, Admin admin) {
    return adminRepo.updateProfile(admin_id, admin);

  }

  public String deleteProfile(String admin_id) {
    return adminRepo.deleteProfile(admin_id);

  }

}
