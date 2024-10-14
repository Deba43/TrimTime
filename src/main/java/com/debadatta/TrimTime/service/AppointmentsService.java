package com.debadatta.TrimTime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.repo.AppointmentsRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppointmentsService {

    @Autowired
    private AppointmentsRepo appointmentsRepo;

     public List<Appointments> getAllAppointments() {
        return appointmentsRepo.fetchAllAppointments();
    }

    public List<Appointments> getAppointmentsForBarber(String barber_id) {
        return appointmentsRepo.fetchAppointmentsForBarber(barber_id);
    }
    
}
