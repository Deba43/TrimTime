package com.debadatta.TrimTime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.service.BarbersService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Barbers")
public class BarbersController {

    @Autowired
    private BarbersService barberService;

    //createProfile
    @PostMapping("/createProfile")
    public ResponseEntity<String> createProfile(@Valid @RequestBody Barbers barbers){
        String barberId =  barberService.createProfile(barbers);
        return ResponseEntity.ok(barberId);
    }

    //updateProfile
    //deleteProfile
    //viewAllAppointments
    //showReviews

    
}
