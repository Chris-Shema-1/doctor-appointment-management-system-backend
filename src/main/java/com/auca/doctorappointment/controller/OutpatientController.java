package com.auca.doctorappointment.controller;

import com.auca.doctorappointment.entity.Outpatient;
import com.auca.doctorappointment.service.OutpatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/outpatients")
public class OutpatientController {

    @Autowired
    private OutpatientService outpatientService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Outpatient outpatient) {
        return new ResponseEntity<>(outpatientService.save(outpatient), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Outpatient>> getAll() {
        return new ResponseEntity<>(outpatientService.getAll(), HttpStatus.OK);
    }

    // B1: GET /api/outpatients/by-last-name?lastName=uwase
    @GetMapping("/by-last-name")
    public ResponseEntity<List<Outpatient>> getByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(outpatientService.getByLastName(lastName), HttpStatus.OK);
    }

    // C4: GET /api/outpatients/of-physician/{physicianId}
    @GetMapping("/of-physician/{physicianId}")
    public ResponseEntity<?> getOfPhysician(@PathVariable UUID physicianId) {
        Optional<List<Outpatient>> outpatients = outpatientService.getOfPhysician(physicianId);
        if (outpatients.isEmpty()) {
            return new ResponseEntity<>("The physician with that id does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(outpatients.get(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Outpatient> outpatient = outpatientService.getById(id);
        if (outpatient.isEmpty()) {
            return new ResponseEntity<>("The outpatient with that id does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(outpatient.get(), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Outpatient outpatient) {
        String message = outpatientService.update(id, outpatient);
        if (message.equals("The outpatient with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = outpatientService.delete(id);
        if (message.equals("The outpatient with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
