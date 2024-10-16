package com.debadatta.TrimTime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.service.CustomersService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Customers")

public class CustomersController {

    @Autowired
    private CustomersService customersService;

    // createProfile
    @PostMapping("/createProfile")
    public ResponseEntity<String> createProfile(@Valid @RequestBody Customers customers) {
        String customerId = customersService.createProfile(customers);
        return ResponseEntity.ok(customerId);
    }

    // updateProfile
    @PutMapping("/updateProfile/{customer_id}")
    public Customers updateProfile(@PathVariable String customer_id, @RequestBody Customers customers) {
        return customersService.updateProfile(customer_id, customers);
    }

    // deleteProfile
    @DeleteMapping("/deleteProfile/{customer_id}")
    public String deleteProfile(@PathVariable String customer_id) {
        return customersService.deleteProfile(customer_id);
    }

    // searchByLocation

    @GetMapping("/searchByLocation/{location}")
    public ResponseEntity<List<Barbers>> searchByLocation(@PathVariable String location) {
        List<Barbers> barbers = customersService.searchByLocation(location);

        if (barbers != null && !barbers.isEmpty()) {
            return ResponseEntity.ok(barbers); // Return barbers at that location
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if no barbers found
        }
    }

    // searchByBarber
    @GetMapping("/searchByBarber{name}")
    public ResponseEntity<List<Barbers>> searchByBarbers(@PathVariable String name) {
        List<Barbers> barbers = customersService.searchByBarbers(name);
        if (barbers != null & !barbers.isEmpty()) {
            return ResponseEntity.ok(barbers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // createAppointment
    @PostMapping("/bookAppointment")
    public ResponseEntity<String> bookAppointment(@RequestBody Appointments appointment) {
        String result = customersService.bookAppointment(appointment);
        return ResponseEntity.ok(result); // Return booking confirmation
    }
    // cancelAppointment
    // rescheduleAppointment

}
