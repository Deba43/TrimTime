package com.debadatta.TrimTime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.service.BarbersService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Barbers")
public class BarbersController {

    @Autowired
    private BarbersService barberService;

    //createProfile
    //updateProfile
    //deleteProfile
    //viewAllAppointments
    //showReviews

    
}
