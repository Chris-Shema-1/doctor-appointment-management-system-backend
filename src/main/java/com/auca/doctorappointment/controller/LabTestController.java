package com.auca.doctorappointment.controller;

import com.auca.doctorappointment.entity.LabTest;
import com.auca.doctorappointment.service.LabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/lab-tests")
public class LabTestController {

    @Autowired
    private LabTestService labTestService;

    // E1: POST /api/lab-tests/save?consultationId=...
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam UUID consultationId, @RequestBody LabTest labTest) {
        String message = labTestService.save(consultationId, labTest);
        if (message.equals("The consultation with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("Lab tests are only for ATTENDED consultations")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }
}
