package com.debadatta.TrimTime.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
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

  public Optional<Barbers> findByPhoneNumber(String phone_no) {
    List<Barbers> bar = dynamoDBMapper.scan(Barbers.class, new DynamoDBScanExpression());
    return bar.stream().filter(user -> user.getPhone_no().equals(phone_no)).findFirst();
  }

  public String deleteProfile(String barber_id) {
    return barbersRepo.deleteProfile(barber_id);

  }

  public Barbers updateProfile(String barber_id, Barbers barbers) {
    return barbersRepo.updateProfile(barber_id, barbers);
  }

}
