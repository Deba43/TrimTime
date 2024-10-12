package com.debadatta.TrimTime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.service.ServicesService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Services")
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    //hairCut
    //hairColoring
    //beardStyling
    //homeServices
    //massage
    //kidsHairCut
    
    
}
