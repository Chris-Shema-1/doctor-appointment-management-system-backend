package com.auca.doctorappointment.controller;

import com.auca.doctorappointment.entity.Consultation;
import com.auca.doctorappointment.entity.ConsultationStatus;
import com.auca.doctorappointment.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        } else if (message.equals("Consultation date cannot be in the past")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if (message.equals("Physician is already booked on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Consultation>> getAll() {
        return new ResponseEntity<>(consultationService.getAll(), HttpStatus.OK);
    }

    // B2: GET /api/consultations/by-status?status=BOOKED
    @GetMapping("/by-status")
    public ResponseEntity<List<Consultation>> getByStatus(@RequestParam ConsultationStatus status) {
        return new ResponseEntity<>(consultationService.getByStatus(status), HttpStatus.OK);
    }

    // B3: GET /api/consultations/between?start=2026-10-01&end=2026-10-31
    @GetMapping("/between")
    public ResponseEntity<List<Consultation>> getBetween(@RequestParam String start, @RequestParam String end) {
        return new ResponseEntity<>(consultationService.getBetween(LocalDate.parse(start), LocalDate.parse(end)),
                HttpStatus.OK);
    }

    // F1: GET /api/consultations/stats/by-status
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStatsByStatus() {
        return new ResponseEntity<>(consultationService.getStatsByStatus(), HttpStatus.OK);
    }

    // F2: PATCH /api/consultations/cancel-day?physicianId=...&date=2026-10-20
    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(@RequestParam UUID physicianId, @RequestParam String date) {
        return new ResponseEntity<>(consultationService.cancelDay(physicianId, LocalDate.parse(date)), HttpStatus.OK);
    }

    // D2: PATCH /api/consultations/{id}/status?status=CHECKED_IN
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id, @RequestParam ConsultationStatus status) {
        String message = consultationService.changeStatus(id, status);
        if (message.equals("The consultation with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.startsWith("Cannot change status from")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
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
        } else if (message.equals("That consultation has lab tests, delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
