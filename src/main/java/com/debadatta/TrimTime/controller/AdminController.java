package com.debadatta.TrimTime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.model.Admin;
import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.serv.AdminService;
import com.debadatta.TrimTime.serv.AppointmentsService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    AppointmentsService appointmentsService;

    // createProfile
    @PostMapping("/createProfile")
    public ResponseEntity<String> createProfile(@Valid @RequestBody Admin admin) {
        String adminId = adminService.createProfile(admin);
        return ResponseEntity.ok(adminId);
    }

    // updateProfile
    @PutMapping("/updateProfile/{admin_id}")
    public Admin updateProfile(@PathVariable String admin_id, @RequestBody Admin admin) {
        return adminService.updateProfile(admin_id, admin);
    }

    // deleteProfile
    @DeleteMapping("/deleteProfile/{admin_id}")
    public String deleteProfile(@PathVariable String admin_id) {
        return adminService.deleteProfile(admin_id);
    }

    // -----Admin-Barber relation

    // approveBarber
    // rejectbarber
    // accountSuspension
    // viewProfiles

    // -----Admin-Customer relation

    // suspendAccount

    // deleteAccount
    // notification
    // viewProfiles

    // -----Admin-Services relation

    // addServices
    // removeServices
    // modifyServices
    // setGlobalPricing

    // -----Admin-Review relation
    // approveReview
    // removeReview

    // -----Admin-Appointment relation
    // viewAllAppointment
    @GetMapping("/getAllAppointments")
    public ResponseEntity<List<Appointments>> getAllAppointments() {
        List<Appointments> appointments = appointmentsService.getAllAppointments();

        if (appointments != null && !appointments.isEmpty()) {
            return ResponseEntity.ok(appointments);
        } else {
            return ResponseEntity.noContent().build(); // Return 204 if no appointments found
        }
    }

    // authorityToCancelAppointment
    // authorityToRescheduleAppointment

    // -----Admin-Content relation
    // Terms-of-Service
    // FAQ
    // Contact-Us
    // Offer

    // -----Admin-Notification
    // System-Maintenance
    // Announcement
    // Missing-Profiles

    // -----
}
