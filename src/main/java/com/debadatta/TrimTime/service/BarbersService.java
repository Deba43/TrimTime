package com.debadatta.TrimTime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.repo.BarbersRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BarbersService {

  @Autowired
  private BarbersRepo barbersRepo;

  public String createProfile(Barbers barbers) {
    return barbersRepo.createProfile(barbers);
  }

  public Barbers updateProfile(String barber_id, Barbers barbers) {
    return barbersRepo.updateProfile(barber_id, barbers);

  }

  public String deleteProfile(String barber_id) {
    return barbersRepo.deleteProfile(barber_id);

  }

}
