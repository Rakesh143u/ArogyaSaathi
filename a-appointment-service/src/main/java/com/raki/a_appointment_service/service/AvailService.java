package com.raki.a_appointment_service.service;

import com.raki.a_appointment_service.model.Availability;
import com.raki.a_appointment_service.repo.AvailabilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AvailService {
    @Autowired
    private AvailabilityRepo repo;
    public ResponseEntity<Availability> setAvail(Availability availability) {
        boolean exists = repo.existsByDoctorIdAndDateAndStartTimeAndEndTime(availability.getDoctorId()
        ,availability.getDate(),availability.getStartTime(),availability.getEndTime());
        if(exists){
            throw new RuntimeException("Availability already exists");
        }
        Availability availability1 = repo.save(availability);
        return new ResponseEntity<>(availability1, HttpStatus.CREATED);
    }
}
