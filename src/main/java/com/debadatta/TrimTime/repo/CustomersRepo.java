package com.debadatta.TrimTime.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.debadatta.TrimTime.model.Barbers;
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

    public List<Barbers> findBarbersByName(String name) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":name", new AttributeValue().withS(name));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(name, :name)") // Use 'contains' for partial name match
                .withExpressionAttributeValues(eav);

        List<Barbers> barbers = dynamoDBMapper.scan(Barbers.class, scanExpression);

        if (barbers == null || barbers.isEmpty()) {
            return new ArrayList<>(); // Return empty list if no matches
        }
        return barbers;
    }

    public List<Barbers> findBarbersByLocation(String location) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":location", new AttributeValue().withS(location));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("location = :location") // Match barbers at the given location
                .withExpressionAttributeValues(eav);

        List<Barbers> barbers = dynamoDBMapper.scan(Barbers.class, scanExpression);

        if (barbers == null || barbers.isEmpty()) {
            return new ArrayList<>(); // Return empty list if no barbers found
        }
        return barbers;
    }

    public Optional<Customers> findByMobileNumber(String mobileNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByMobileNumber'");
    }

}
