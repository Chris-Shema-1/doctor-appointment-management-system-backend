package com.auca.doctorappointment.service;

import com.auca.doctorappointment.entity.MedicalField;
import com.auca.doctorappointment.repository.MedicalFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MedicalFieldService {

    @Autowired
    private MedicalFieldRepository medicalFieldRepository;

    public String save(MedicalField medicalField) {
        medicalField.setId(null);
        medicalFieldRepository.save(medicalField);
        return "Medical field saved successfully";
    }

    public List<MedicalField> getAll() {
        return medicalFieldRepository.findAll();
    }

    // Optional.empty() means the medical field does not exist
    public Optional<MedicalField> getById(UUID id) {
        return medicalFieldRepository.findById(id);
    }

    public String update(UUID id, MedicalField newData) {
        Optional<MedicalField> medicalField = medicalFieldRepository.findById(id);
        if (medicalField.isEmpty()) {
            return "The medical field with that id does not exist";
        }
        medicalField.get().setName(newData.getName());
        medicalFieldRepository.save(medicalField.get());
        return "Medical field updated successfully";
    }

    public String delete(UUID id) {
        Optional<MedicalField> medicalField = medicalFieldRepository.findById(id);
        if (medicalField.isEmpty()) {
            return "The medical field with that id does not exist";
        }
        medicalFieldRepository.delete(medicalField.get());
        return "Medical field deleted successfully";
    }
}
