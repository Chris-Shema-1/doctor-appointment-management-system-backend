package com.auca.doctorappointment.service;

import com.auca.doctorappointment.entity.Outpatient;
import com.auca.doctorappointment.repository.OutpatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OutpatientService {

    @Autowired
    private OutpatientRepository outpatientRepository;

    public String save(Outpatient outpatient) {
        outpatient.setId(null);
        outpatientRepository.save(outpatient);
        return "Outpatient saved successfully";
    }

    public List<Outpatient> getAll() {
        return outpatientRepository.findAll();
    }

    // Optional.empty() means the outpatient does not exist
    public Optional<Outpatient> getById(UUID id) {
        return outpatientRepository.findById(id);
    }

    public String update(UUID id, Outpatient newData) {
        Optional<Outpatient> outpatient = outpatientRepository.findById(id);
        if (outpatient.isEmpty()) {
            return "The outpatient with that id does not exist";
        }
        outpatient.get().setFirstName(newData.getFirstName());
        outpatient.get().setLastName(newData.getLastName());
        outpatient.get().setNationalId(newData.getNationalId());
        outpatient.get().setPhone(newData.getPhone());
        outpatient.get().setEmail(newData.getEmail());
        outpatientRepository.save(outpatient.get());
        return "Outpatient updated successfully";
    }

    public String delete(UUID id) {
        Optional<Outpatient> outpatient = outpatientRepository.findById(id);
        if (outpatient.isEmpty()) {
            return "The outpatient with that id does not exist";
        }
        outpatientRepository.delete(outpatient.get());
        return "Outpatient deleted successfully";
    }
}
