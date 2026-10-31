package com.auca.doctorappointment.service;

import com.auca.doctorappointment.entity.Consultation;
import com.auca.doctorappointment.entity.ConsultationStatus;
import com.auca.doctorappointment.entity.LabTest;
import com.auca.doctorappointment.repository.ConsultationRepository;
import com.auca.doctorappointment.repository.LabTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LabTestService {

    @Autowired
    private LabTestRepository labTestRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    // E1: body {"testName":..., "findings":..., "turnaroundHours":24}; only for ATTENDED consultations
    public String save(UUID consultationId, LabTest labTest) {
        Optional<Consultation> consultation = consultationRepository.findById(consultationId);
        if (consultation.isEmpty()) {
            return "The consultation with that id does not exist";
        }
        if (consultation.get().getStatus() != ConsultationStatus.ATTENDED) {
            return "Lab tests are only for ATTENDED consultations";
        }

        // the service sets the date and the link, not the client
        labTest.setId(null);
        labTest.setOrderedDate(LocalDate.now());
        labTest.setConsultation(consultation.get());
        labTestRepository.save(labTest);
        return "Lab test saved successfully";
    }

    // E2: empty list when the outpatient has no lab tests
    public List<LabTest> getByOutpatient(String nationalId) {
        return labTestRepository.findByOutpatientNationalId(nationalId);
    }
}
