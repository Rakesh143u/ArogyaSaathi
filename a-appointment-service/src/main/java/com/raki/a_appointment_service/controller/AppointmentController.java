package com.raki.a_appointment_service.controller;


import com.raki.a_appointment_service.Status;
import com.raki.a_appointment_service.model.Appointment;
import com.raki.a_appointment_service.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestHeader("X-User-Email") String email, @RequestBody Appointment appointment){
        appointment.setPatientEmail(email);
        appointment.setStatus(Status.PENDING);
        return appointmentService.bookAppointment(appointment);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Appointment>> getMyAppointments(@RequestHeader("X-User-Email") String email){
        return appointmentService.getMyAppointments(email);
    }
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable Long doctorId){
        return appointmentService.getAppointments(doctorId);
    }
    @PutMapping("confirm/{id}")
    public ResponseEntity<Appointment> confirmAppointment(@PathVariable Long id,@RequestHeader("X-User-Id") Long doctorId){
        return appointmentService.confirmAppointment(id,doctorId);
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long id,@RequestHeader("X-User-Email") String email){
        return appointmentService.cancelAppointment(id,email);
    }

    @PutMapping("complete/{id}")
    public ResponseEntity<Appointment> completeAppointment(@PathVariable Long id,@RequestHeader("X-User-Id") Long doctorId){
        return appointmentService.completeAppointment(id,doctorId);
    }
}
