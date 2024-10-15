package com.debadatta.TrimTime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.repo.CustomersRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomersService {

  @Autowired
  private CustomersRepo customersRepo;

  public String createProfile(Customers customers) {
    return customersRepo.createProfile(customers);
  }

  public Customers updateProfile(String customer_id, Customers customers) {
    return customersRepo.updateProfile(customer_id, customers);

  }

  public String deleteProfile(String customer_id) {
    return customersRepo.deleteProfile(customer_id);

  }

  public List<Barbers> searchByBarbers(String name) {
    return customersRepo.findBarbersByName(name);
  }

}
