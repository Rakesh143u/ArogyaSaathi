package com.raki.a_appointment_service.controller;


import com.raki.a_appointment_service.model.Availability;
import com.raki.a_appointment_service.service.AvailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avail")
public class AvailabilityController {
    @Autowired
    private AvailService service;
    @PostMapping()
    public ResponseEntity<Availability> setAvail(@RequestBody Availability availability){
        return service.setAvail(availability);
    }
}
