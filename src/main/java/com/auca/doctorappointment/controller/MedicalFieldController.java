package com.auca.doctorappointment.controller;

import com.auca.doctorappointment.entity.MedicalField;
import com.auca.doctorappointment.service.MedicalFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/medical-fields")
public class MedicalFieldController {

    @Autowired
    private MedicalFieldService medicalFieldService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody MedicalField medicalField) {
        return new ResponseEntity<>(medicalFieldService.save(medicalField), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MedicalField>> getAll() {
        return new ResponseEntity<>(medicalFieldService.getAll(), HttpStatus.OK);
    }

    // C3: GET /api/medical-fields/unused
    @GetMapping("/unused")
    public ResponseEntity<List<MedicalField>> getUnused() {
        return new ResponseEntity<>(medicalFieldService.getUnused(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<MedicalField> medicalField = medicalFieldService.getById(id);
        if (medicalField.isEmpty()) {
            return new ResponseEntity<>("The medical field with that id does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(medicalField.get(), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody MedicalField medicalField) {
        String message = medicalFieldService.update(id, medicalField);
        if (message.equals("The medical field with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = medicalFieldService.delete(id);
        if (message.equals("The medical field with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That medical field is held by a physician, remove it from the physician first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
