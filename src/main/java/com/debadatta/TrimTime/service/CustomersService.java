package com.debadatta.TrimTime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.debadatta.TrimTime.dto.CustomerRegistrationRequest;
import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.repo.AppointmentsRepo;
import com.debadatta.TrimTime.repo.CustomersRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomersService {

  @Autowired
  private CustomersRepo customersRepo;
  @Autowired
  private AppointmentsRepo appointmentsRepo;
  @Autowired
  final private DynamoDBMapper dynamoDBMapper;

  public String createProfile(Customers customers) {
    return customersRepo.createProfile(customers);
  }

  
  public Customers registerCustomer(CustomerRegistrationRequest request) {
    Customers customer = new Customers();
    customer.setName(request.getName());
    customer.setAge(request.getAge());
    customer.setMobileNumber(request.getMobileNumber());
    customer.setEmail(request.getEmail());
    customer.setProfilePictureUrl(request.getProfilePictureUrl());

    dynamoDBMapper.save(customer);

    return customer;
  }

  public String deleteProfile(String customer_id) {
    return customersRepo.deleteProfile(customer_id);

  }

  public List<Barbers> searchByBarbers(String name) {
    return customersRepo.findBarbersByName(name);
  }

  public List<Barbers> searchByLocation(String location) {
    return customersRepo.findBarbersByLocation(location);
  }

  public String bookAppointment(Appointments appointment) {
    return appointmentsRepo.bookAppointment(appointment);
  }

  public String cancelAppointment(Appointments appointment) {
    return appointmentsRepo.cancelAppointment(appointment);
  }

public Customers updateProfile(String customer_id, Customers customers) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateProfile'");
}

}
