package com.raki.a_appointment_service.repo;


import com.raki.a_appointment_service.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AvailabilityRepo extends JpaRepository<Availability,Long> {
    boolean existsByDoctorIdAndDateAndStartTimeAndEndTime(Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
