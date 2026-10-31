package com.auca.doctorappointment.controller;

import com.auca.doctorappointment.entity.Physician;
import com.auca.doctorappointment.service.PhysicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/physicians")
public class PhysicianController {

    @Autowired
    private PhysicianService physicianService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Physician physician) {
        String message = physicianService.save(physician);
        if (message.equals("The consulting room with that id does not exist")
                || message.equals("That medical field does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Physician>> getAll() {
        return new ResponseEntity<>(physicianService.getAll(), HttpStatus.OK);
    }

    // C1: GET /api/physicians/by-medical-field?name=cardiology
    @GetMapping("/by-medical-field")
    public ResponseEntity<List<Physician>> getByMedicalField(@RequestParam String name) {
        return new ResponseEntity<>(physicianService.getByMedicalField(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Physician> physician = physicianService.getById(id);
        if (physician.isEmpty()) {
            return new ResponseEntity<>("The physician with that id does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(physician.get(), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Physician physician) {
        String message = physicianService.update(id, physician);
        if (message.equals("The physician with that id does not exist")
                || message.equals("The consulting room with that id does not exist")
                || message.equals("That medical field does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = physicianService.delete(id);
        if (message.equals("The physician with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
