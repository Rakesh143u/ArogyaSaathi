package com.raki.a_appointment_service.service;

import com.raki.a_appointment_service.model.Appointment;
import com.raki.a_appointment_service.model.Availability;
import com.raki.a_appointment_service.repo.AppointmentRepo;
import com.raki.a_appointment_service.repo.AvailabilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private AvailabilityRepo availabilityRepo;


    public ResponseEntity<Appointment> bookAppointment(Appointment appointment) {
        boolean exists = appointmentRepo.existsByDoctorIdAndDateAndTime(appointment.getDoctorId(),appointment.getDate(),appointment.getTime());
        if(exists){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Availability availability = availabilityRepo.findAll().stream().filter(
                a->a.getDoctorId().equals(appointment.getDoctorId())&&a.getDate().equals(appointment.getDate())&&!appointment.getTime().isBefore(a.getStartTime())&&!appointment.getTime().isAfter(a.getEndTime())).findFirst().orElseThrow(()->new RuntimeException("Doctor Not Available at This Time"));
         Appointment saved = appointmentRepo.save(appointment);
        return new ResponseEntity<>(saved,HttpStatus.CREATED);

    }
}
