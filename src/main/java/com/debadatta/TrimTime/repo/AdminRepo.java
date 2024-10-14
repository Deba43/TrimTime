package com.debadatta.TrimTime.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.debadatta.TrimTime.model.Admin;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class AdminRepo {

    @Autowired
    final private DynamoDBMapper dynamoDBMapper;

    public String createProfile(Admin admin) {
        dynamoDBMapper.save(admin);
        return admin.getAdmin_id();
    }

    public Admin updateProfile(String admin_id, Admin admin) {
        Admin load = dynamoDBMapper.load(Admin.class, admin_id);
        if (load == null) {
            throw new RuntimeException("Your ID " + admin_id + " not found");
        }
        if (admin.getName() != null)
            load.setName(admin.getName());
        if (admin.getMobileNumber() != null)
            load.setMobileNumber(admin.getMobileNumber());
        if (admin.getEmail() != null)
            load.setEmail(admin.getEmail());
        if (admin.getPassword() != null)
            load.setPassword(admin.getPassword());
        if (admin.getRole() != null)
            load.setRole(admin.getRole());
        if (admin.getAccountStatus() != null)
            load.setAccountStatus(admin.getAccountStatus());
        if (admin.getPermissions() != null)
            load.setPermissions(admin.getPermissions());
        //if (admin.isTwoFactorEnabled() == false)
            load.setTwoFactorEnabled(admin.isTwoFactorEnabled());
        if (admin.getTwofactorSecret() != null)
            load.setTwofactorSecret(admin.getTwofactorSecret());
        if (admin.getProfilePictureUrl() != null)
            load.setProfilePictureUrl(admin.getProfilePictureUrl());

        dynamoDBMapper.save(load);

        return dynamoDBMapper.load(Admin.class, admin_id);

    }

    public String deleteProfile(String admin_id) {
        Admin load = dynamoDBMapper.load(Admin.class, admin_id);
        if (load == null) {
            throw new RuntimeException("Your ID " + admin_id + " not found");
        }
        dynamoDBMapper.delete(load);
        return "Admin profile for " + load.getName() + " has been deleted successfully.";
    }
}
