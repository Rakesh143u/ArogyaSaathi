package com.raki.a_appointment_service.service;

import com.raki.a_appointment_service.Status;
import com.raki.a_appointment_service.model.Appointment;
import com.raki.a_appointment_service.model.Availability;
import com.raki.a_appointment_service.repo.AppointmentRepo;
import com.raki.a_appointment_service.repo.AvailabilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ResponseEntity<List<Appointment>> getMyAppointments(String email){
        return new ResponseEntity<>(appointmentRepo.findByPatientEmail(email),HttpStatus.OK);
    }

    public ResponseEntity<List<Appointment>> getAppointments(Long doctorId) {
        return new ResponseEntity<>(appointmentRepo.findByDoctorId(doctorId),HttpStatus.OK);
    }

    public  ResponseEntity<Appointment> confirmAppointment(Long id,Long doctorId){
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(()->new RuntimeException("Appointment Not Found"));
        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new RuntimeException("Access Denied");
        }
        if(appointment.getStatus()!= Status.PENDING){
            throw new RuntimeException("Only Pending Appointment Can be Confirmed");
        }
        appointment.setStatus(Status.CONFIRMED);
        return new ResponseEntity<>(appointmentRepo.save(appointment),HttpStatus.OK);
    }

    public ResponseEntity<Appointment> cancelAppointment(Long id,String email) {
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(()->new RuntimeException("Appointment Not Found"));
        if(!appointment.getPatientEmail().equals(email)){
            throw new RuntimeException("You Can only Cancel your own Appointment");
        }
        if(appointment.getStatus()==Status.COMPLETED) {
            throw new RuntimeException("Completed Appointments Cannot be Cancelled");
        }
        appointment.setStatus(Status.CANCELLED);
        return new ResponseEntity<>(appointmentRepo.save(appointment),HttpStatus.OK);
    }
    public ResponseEntity<Appointment> completeAppointment(Long id,Long doctorId) {
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(()->new RuntimeException("Appointment Not Found"));
        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new RuntimeException("Access Denied");
        }
        if(appointment.getStatus()!=Status.CONFIRMED){
            throw new RuntimeException("Only Confirmed Appointment Can be Complete");
        }
        appointment.setStatus(Status.COMPLETED);
        return new ResponseEntity<>(appointmentRepo.save(appointment),HttpStatus.OK);
    }
}
