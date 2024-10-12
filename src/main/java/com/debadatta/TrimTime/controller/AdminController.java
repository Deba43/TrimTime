package com.debadatta.TrimTime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.service.AdminService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // createAdmin
    // updateAdmin
    // deleteAdmin

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
