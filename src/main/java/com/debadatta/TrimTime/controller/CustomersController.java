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

import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.service.AppointmentsService;

import com.debadatta.TrimTime.service.CustomersService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/Customer")

public class CustomersController {

    @Autowired
    private CustomersService customersService;
    @Autowired
    private AppointmentsService appointmentsService;

    public CustomersController(CustomersService customersService) {
        this.customersService = customersService;
    }

   

    // updateProfile
    @PutMapping("dashboard/{customer_id}")
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
    public ResponseEntity<List<Barbers>> searchByLocation(@RequestParam String query) {
        List<Barbers> barbers = customersService.searchByLocation(query);

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
    @PostMapping("/cancelAppointment")
    public ResponseEntity<String> cancelAppointment(@RequestBody Appointments appointment) {
        String result = customersService.cancelAppointment(appointment);
        return ResponseEntity.ok(result); // Return cancellation confirmation
    }

    // rescheduleAppointment
    @PutMapping("/rescheduleAppointment/{appointment_id}")
    public ResponseEntity<String> rescheduleAppointment(@PathVariable String appointment_id,
            @RequestBody Appointments updatedAppointment) {

        boolean isUpdated = appointmentsService.rescheduleAppointment(appointment_id, updatedAppointment);

        if (isUpdated) {
            return ResponseEntity.ok("Appointment rescheduled successfully");
        } else {
            return ResponseEntity.badRequest().body("Barber is unavailable or appointment could not be updated");
        }

    }

}
