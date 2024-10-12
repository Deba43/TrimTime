package com.debadatta.TrimTime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.service.CustomersService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Customers")

public class CustomersController {
    
    @Autowired
    private CustomersService customersService;

    // createCustomer
    // updateCustomer
    // deleteCustomer
    // searchByLocation
    // createAppointment
    // cancelAppointment
    // rescheduleAppointment
    // searchByBarber

}
