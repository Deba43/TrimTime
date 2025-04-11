package com.debadatta.TrimTime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.serv.AppointmentsService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Appointments")
public class AppointmentsController {

    @Autowired
    private AppointmentsService appointmentsService;

    //booked
    //rescheduled
    //cancelled
    
}
