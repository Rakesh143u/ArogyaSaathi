package com.raki.a_appointment_service.repo;

import com.raki.a_appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
    boolean existsByDoctorIdAndDateAndTime(Long doctorId, LocalDate date, LocalTime time);
}
