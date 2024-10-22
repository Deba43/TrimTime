package com.debadatta.TrimTime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.model.Appointments;
import com.debadatta.TrimTime.model.Barbers;
import com.debadatta.TrimTime.repo.AppointmentsRepo;
import com.debadatta.TrimTime.repo.BarbersRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppointmentsService {

    @Autowired
    private AppointmentsRepo appointmentsRepo;
    @Autowired
    private BarbersRepo barbersRepo;

    public List<Appointments> getAllAppointments() {
        return appointmentsRepo.fetchAllAppointments();
    }

    public List<Appointments> getAppointmentsForBarber(String barber_id) {
        return appointmentsRepo.fetchAppointmentsForBarber(barber_id);
    }

    public boolean rescheduleAppointment(String appointment_id, Appointments updatedAppointments) {

        Appointments existingAppointment = appointmentsRepo.getAppointmentId(appointment_id);
        if (existingAppointment == null)
            return false;

        Barbers barber = barbersRepo.getBarbersById(updatedAppointments.getBarber_id());

        if (barber != null && barber.isAvailable(updatedAppointments.getAppointment_date(),
                updatedAppointments.getAppointment_time())) {

            existingAppointment.setAppointment_date(updatedAppointments.getAppointment_date());
            existingAppointment.setAppointment_time(updatedAppointments.getAppointment_time());
            existingAppointment.setAppointment_status("Rescheduled");

            appointmentsRepo.save(existingAppointment);

            return true;
        } else {
            return false;
        }
    }

}
