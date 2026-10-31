package com.auca.doctorappointment.controller;

import com.auca.doctorappointment.entity.Consultation;
import com.auca.doctorappointment.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Consultation consultation) {
        String message = consultationService.save(consultation);
        if (message.equals("The outpatient with that id does not exist")
                || message.equals("The physician with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Consultation>> getAll() {
        return new ResponseEntity<>(consultationService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Consultation> consultation = consultationService.getById(id);
        if (consultation.isEmpty()) {
            return new ResponseEntity<>("The consultation with that id does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(consultation.get(), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Consultation consultation) {
        String message = consultationService.update(id, consultation);
        if (message.equals("The consultation with that id does not exist")
                || message.equals("The outpatient with that id does not exist")
                || message.equals("The physician with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = consultationService.delete(id);
        if (message.equals("The consultation with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
