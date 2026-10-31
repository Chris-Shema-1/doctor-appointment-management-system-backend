package com.auca.doctorappointment.service;

import com.auca.doctorappointment.entity.Consultation;
import com.auca.doctorappointment.entity.ConsultationStatus;
import com.auca.doctorappointment.entity.Outpatient;
import com.auca.doctorappointment.entity.Physician;
import com.auca.doctorappointment.repository.ConsultationRepository;
import com.auca.doctorappointment.repository.OutpatientRepository;
import com.auca.doctorappointment.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private OutpatientRepository outpatientRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    // body: {"consultDate":"2026-11-05", "complaint":..., "outpatient":{"id":"..."}, "physician":{"id":"..."}}
    public String save(Consultation consultation) {
        String problem = loadOutpatientAndPhysician(consultation, consultation);
        if (problem != null) {
            return problem;
        }

        // D1: no consultations in the past
        if (consultation.getConsultDate().isBefore(LocalDate.now())) {
            return "Consultation date cannot be in the past";
        }
        // D1: the physician must not already have a non-cancelled consultation that day
        if (consultationRepository.existsByPhysicianAndConsultDateAndStatusNot(
                consultation.getPhysician(), consultation.getConsultDate(), ConsultationStatus.CANCELLED)) {
            return "Physician is already booked on that date";
        }

        // D1: a new consultation always starts as BOOKED, whatever status the JSON body contains
        consultation.setStatus(ConsultationStatus.BOOKED);
        consultation.setId(null);
        consultationRepository.save(consultation);
        return "Consultation saved successfully";
    }

    public List<Consultation> getAll() {
        return consultationRepository.findAll();
    }

    // Optional.empty() means the consultation does not exist
    public Optional<Consultation> getById(UUID id) {
        return consultationRepository.findById(id);
    }

    // B2
    public List<Consultation> getByStatus(ConsultationStatus status) {
        return consultationRepository.findByStatusOrderByConsultDateAsc(status);
    }

    // B3: the controller has already turned the Strings into LocalDate
    public List<Consultation> getBetween(LocalDate start, LocalDate end) {
        return consultationRepository.findByConsultDateBetweenOrderByConsultDateAsc(start, end);
    }

    public String update(UUID id, Consultation newData) {
        Optional<Consultation> consultation = consultationRepository.findById(id);
        if (consultation.isEmpty()) {
            return "The consultation with that id does not exist";
        }
        String problem = loadOutpatientAndPhysician(newData, consultation.get());
        if (problem != null) {
            return problem;
        }
        consultation.get().setConsultDate(newData.getConsultDate());
        consultation.get().setComplaint(newData.getComplaint());
        consultation.get().setStatus(newData.getStatus());
        consultationRepository.save(consultation.get());
        return "Consultation updated successfully";
    }

    public String delete(UUID id) {
        Optional<Consultation> consultation = consultationRepository.findById(id);
        if (consultation.isEmpty()) {
            return "The consultation with that id does not exist";
        }
        consultationRepository.delete(consultation.get());
        return "Consultation deleted successfully";
    }

    // looks up the outpatient and physician sent by id in `from` and puts the real ones on `target`;
    // returns a not-found message, or null when both exist
    private String loadOutpatientAndPhysician(Consultation from, Consultation target) {

        if (from.getOutpatient() == null || from.getOutpatient().getId() == null) {
            return "The outpatient with that id does not exist";
        }
        Optional<Outpatient> outpatient = outpatientRepository.findById(from.getOutpatient().getId());
        if (outpatient.isEmpty()) {
            return "The outpatient with that id does not exist";
        }

        if (from.getPhysician() == null || from.getPhysician().getId() == null) {
            return "The physician with that id does not exist";
        }
        Optional<Physician> physician = physicianRepository.findById(from.getPhysician().getId());
        if (physician.isEmpty()) {
            return "The physician with that id does not exist";
        }

        target.setOutpatient(outpatient.get());
        target.setPhysician(physician.get());
        return null;
    }
}
