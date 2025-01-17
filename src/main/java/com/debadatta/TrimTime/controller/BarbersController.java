package com.debadatta.TrimTime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.dto.BarberRegistrationRequest;
import com.debadatta.TrimTime.dto.CustomerRegistrationRequest;
import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.model.Reviews;
import com.debadatta.TrimTime.service.AppointmentsService;
import com.debadatta.TrimTime.service.BarbersService;
import com.debadatta.TrimTime.service.ReviewsService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/Barber")
public class BarbersController {

    @Autowired
    private BarbersService barbersService;
    @Autowired
    AppointmentsService appointmentsService;
    @Autowired
    ReviewsService reviewsService;

    public BarbersController(BarbersService barbersService) {
        this.barbersService = barbersService;
    }

    @PostMapping("/sendOTP")
    public ResponseEntity<String> sendOTP(@RequestParam String mobileNumber) {
        String message = barbersService.generateAndSendOTP(mobileNumber);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/Barber-registration")
    public ResponseEntity<Barbers> registerBarber(
            @RequestParam String mobileNumber,
            @RequestParam String otp,
            @RequestBody BarberRegistrationRequest request) {

        try {
            Barbers registeredBarber = barbersService.verifyOTPAndRegister(mobileNumber, otp, request);
            return ResponseEntity.ok(registeredBarber);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    // updateProfile
    @PutMapping("/dashboard/{barber_id}")
    public Barbers updateProfile(@PathVariable String barber_id, @RequestBody Barbers barbers) {
        return barbersService.updateProfile(barber_id, barbers);
    }

    // deleteProfile
    @DeleteMapping("/deleteProfile/{barber_id}")
    public String deleteProfile(@PathVariable String barber_id) {
        return barbersService.deleteProfile(barber_id);
    }

    // getMyAppointments
    @GetMapping("/getMyAppointments{barber_id}")
    public ResponseEntity<List<Appointments>> getAppointmentsForBarber(@PathVariable String barber_id) {
        List<Appointments> appointments = appointmentsService.getAppointmentsForBarber(barber_id);

        if (appointments != null && !appointments.isEmpty()) {
            return ResponseEntity.ok(appointments);
        } else {
            return ResponseEntity.noContent().build(); // Return 204 if no appointments found
        }
    }
    // showMyReviews

    @GetMapping("/showMyReviews/{barber_id}") // Fix the PathVariable syntax
    public ResponseEntity<List<Reviews>> showMyReviews(@PathVariable String barber_id) {
        List<Reviews> reviews = reviewsService.showMyReviews(barber_id);

        if (reviews != null && !reviews.isEmpty()) {
            return ResponseEntity.ok(reviews);
        } else {
            return ResponseEntity.noContent().build(); // Return 204 if no reviews found
        }
    }

}
