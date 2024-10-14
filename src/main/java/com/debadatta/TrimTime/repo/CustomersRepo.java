package com.debadatta.TrimTime.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.debadatta.TrimTime.model.Customers;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CustomersRepo {

    @Autowired
    final private DynamoDBMapper dynamoDBMapper;

    public String createProfile(Customers customers) {
        dynamoDBMapper.save(customers);
        return customers.getCustomer_id();
    }

    public Customers updateProfile(String customer_id, Customers customers) {
        Customers existingCustomers = dynamoDBMapper.load(Customers.class, customer_id);

        if (existingCustomers == null) {
            throw new RuntimeException("Your ID " + customer_id + " not found");
        }
        if (existingCustomers.getName() != null)
            existingCustomers.setName(customers.getName());
        if (existingCustomers.getMobileNumber() != null)
            existingCustomers.setMobileNumber(customers.getMobileNumber());
        if (existingCustomers.getPassword() != null)
            existingCustomers.setPassword(customers.getPassword());
        if (existingCustomers.getProfilePictureUrl() != null)
            existingCustomers.setProfilePictureUrl(customers.getProfilePictureUrl());

        dynamoDBMapper.save(existingCustomers);

        return dynamoDBMapper.load(Customers.class, customer_id);

    }

    public String deleteProfile(String customer_id) {
        Customers existingCustomers = dynamoDBMapper.load(Customers.class, customer_id);
        if (existingCustomers == null) {
            throw new RuntimeException("Your ID " + customer_id + " not found");
        }
        dynamoDBMapper.delete(existingCustomers);
        return "Customer profile for " + existingCustomers.getName() + " has been deleted successfully.";
    }

}
