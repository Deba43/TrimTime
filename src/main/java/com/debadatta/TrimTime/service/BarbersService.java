package com.debadatta.TrimTime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.debadatta.TrimTime.dto.BarberRegistrationRequest;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.repo.BarbersRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BarbersService {

  @Autowired
  private BarbersRepo barbersRepo;
  @Autowired
  final private DynamoDBMapper dynamoDBMapper;

  public String createProfile(Barbers barbers) {
    return barbersRepo.createProfile(barbers);
  }

  
  public Barbers registerBarber(BarberRegistrationRequest request) {
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateProfile'");
}

}
