package com.debadatta.TrimTime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Reviews;
import com.debadatta.TrimTime.serv.AppointmentsService;
import com.debadatta.TrimTime.serv.BarbersService;
import com.debadatta.TrimTime.serv.ReviewsService;

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
