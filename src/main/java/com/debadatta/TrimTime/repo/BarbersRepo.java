package com.debadatta.TrimTime.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.debadatta.TrimTime.model.Barbers;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class BarbersRepo {

    @Autowired
    final private DynamoDBMapper dynamoDBMapper;

    public String createProfile(Barbers barbers) {
        dynamoDBMapper.save(barbers);
        return barbers.getBarber_id();
    }

    public Barbers updateProfile(String barber_id, Barbers barbers) {
        Barbers existingBarbers = dynamoDBMapper.load(Barbers.class, barber_id);

        if (existingBarbers == null) {
            throw new RuntimeException("Your ID " + barber_id + " not found");
        }
        if (barbers.getName() != null)
            existingBarbers.setName(barbers.getName());
        if (barbers.getMobileNumber() != null)
            existingBarbers.setMobileNumber(barbers.getMobileNumber());
        if (barbers.getPassword() != null)
            existingBarbers.setPassword(barbers.getPassword());
        if (barbers.getBarberShopName() != null)
            existingBarbers.setBarberShopName(barbers.getBarberShopName());
        if (barbers.getLocation() != null)
            existingBarbers.setLocation(barbers.getLocation());
        if (barbers.getAvailability() != null)
            existingBarbers.setAvailability(barbers.getAvailability());
        if (barbers.getExperience() != null)
            existingBarbers.setExperience(barbers.getExperience());
        if (barbers.getRole() != null)
            existingBarbers.setRole(barbers.getRole());
        if (barbers.getBio() != null)
            existingBarbers.setBio(barbers.getBio());
        if (barbers.getProfilePictureUrl() != null)
            existingBarbers.setProfilePictureUrl(barbers.getProfilePictureUrl());

        dynamoDBMapper.save(existingBarbers);

        return dynamoDBMapper.load(Barbers.class, barber_id);

    }

    public String deleteProfile(String barber_id) {
        Barbers exitingBarbers = dynamoDBMapper.load(Barbers.class, barber_id);
        if (exitingBarbers == null) {
            throw new RuntimeException("Your ID " + barber_id + " not found");
        }
        dynamoDBMapper.delete(exitingBarbers);
        return "Barber profile for " + exitingBarbers.getName() + " has been deleted successfully.";
    }

    public Barbers getBarbersById(String barber_id) {
        return dynamoDBMapper.load(Barbers.class, barber_id);
    }

}
