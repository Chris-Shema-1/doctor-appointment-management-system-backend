package com.auca.doctorappointment.controller;

import com.auca.doctorappointment.entity.LabTest;
import com.auca.doctorappointment.service.LabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // E2: GET /api/lab-tests/outpatient/{nationalId}
    @GetMapping("/outpatient/{nationalId}")
    public ResponseEntity<List<LabTest>> getByOutpatient(@PathVariable String nationalId) {
        return new ResponseEntity<>(labTestService.getByOutpatient(nationalId), HttpStatus.OK);
    }

    // E3: DELETE /api/lab-tests/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = labTestService.delete(id);
        if (message.equals("The lab test with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
