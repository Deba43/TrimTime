package com.debadatta.TrimTime.serv;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
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

  public Optional<Customers> findByPhoneNumber(String phone_no) {
        List<Customers> cus = dynamoDBMapper.scan(Customers.class, new DynamoDBScanExpression());
        return cus.stream().filter(user -> user.getPhone_no().equals(phone_no)).findFirst();
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
    return customersRepo.updateProfile(customer_id, customers);
  }

}
