package com.auca.doctorappointment.service;

import com.auca.doctorappointment.entity.ConsultingRoom;
import com.auca.doctorappointment.entity.MedicalField;
import com.auca.doctorappointment.entity.Physician;
import com.auca.doctorappointment.repository.ConsultationRepository;
import com.auca.doctorappointment.repository.ConsultingRoomRepository;
import com.auca.doctorappointment.repository.MedicalFieldRepository;
import com.auca.doctorappointment.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhysicianService {

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private ConsultingRoomRepository consultingRoomRepository;

    @Autowired
    private MedicalFieldRepository medicalFieldRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    // body: {"firstName":..., "consultingRoom":{"id":"..."}, "medicalFields":[{"id":"..."}]}
    public String save(Physician physician) {
        String problem = loadRoomAndFields(physician, physician);
        if (problem != null) {
            return problem;
        }
        physician.setId(null);
        physicianRepository.save(physician);
        return "Physician saved successfully";
    }

    public List<Physician> getAll() {
        return physicianRepository.findAll();
    }

    // Optional.empty() means the physician does not exist
    public Optional<Physician> getById(UUID id) {
        return physicianRepository.findById(id);
    }

    // C1
    public List<Physician> getByMedicalField(String name) {
        return physicianRepository.findByMedicalFieldName(name);
    }

    public String update(UUID id, Physician newData) {
        Optional<Physician> physician = physicianRepository.findById(id);
        if (physician.isEmpty()) {
            return "The physician with that id does not exist";
        }
        String problem = loadRoomAndFields(newData, physician.get());
        if (problem != null) {
            return problem;
        }
        physician.get().setFirstName(newData.getFirstName());
        physician.get().setLastName(newData.getLastName());
        physician.get().setLicenceNumber(newData.getLicenceNumber());
        physicianRepository.save(physician.get());
        return "Physician updated successfully";
    }

    // D3: add one medical field and keep the ones already there; Physician owns the link, so we save the physician
    @Transactional
    public String addMedicalField(UUID physicianId, UUID medicalFieldId) {
        Optional<Physician> physician = physicianRepository.findById(physicianId);
        if (physician.isEmpty()) {
            return "The physician with that id does not exist";
        }
        Optional<MedicalField> medicalField = medicalFieldRepository.findById(medicalFieldId);
        if (medicalField.isEmpty()) {
            return "That medical field does not exist";
        }
        if (physician.get().getMedicalFields().contains(medicalField.get())) {
            return "The physician already has that medical field";
        }

        physician.get().getMedicalFields().add(medicalField.get());
        physicianRepository.save(physician.get());
        return "Medical field added to the physician";
    }

    // D4: give the physician the room with that roomNumber; the old room becomes free (check with C2)
    public String changeConsultingRoom(UUID physicianId, int roomNumber) {
        Optional<Physician> physician = physicianRepository.findById(physicianId);
        if (physician.isEmpty()) {
            return "The physician with that id does not exist";
        }
        Optional<ConsultingRoom> room = consultingRoomRepository.findByRoomNumber(roomNumber);
        if (room.isEmpty()) {
            return "The consulting room with that roomNumber does not exist";
        }

        // taken only counts when someone else is in it; a physician asking for their own room is not blocked
        ConsultingRoom current = physician.get().getConsultingRoom();
        boolean alreadyMine = current != null && current.getId().equals(room.get().getId());
        if (physicianRepository.existsByConsultingRoom(room.get()) && !alreadyMine) {
            return "That consulting room is taken by another physician";
        }

        physician.get().setConsultingRoom(room.get());
        physicianRepository.save(physician.get());
        return "Consulting room changed successfully";
    }

    public String delete(UUID id) {
        Optional<Physician> physician = physicianRepository.findById(id);
        if (physician.isEmpty()) {
            return "The physician with that id does not exist";
        }
        // consultation.physician_id still points at this physician; deleting would fail.
        // (the physician_medical_field rows are removed by Hibernate, because Physician owns that link)
        if (consultationRepository.existsByPhysician(physician.get())) {
            return "That physician has consultations, delete them first";
        }
        physicianRepository.delete(physician.get());
        return "Physician deleted successfully";
    }

    // looks up the consulting room and medical fields sent by id in `from` and puts the real ones on `target`;
    // returns a not-found message, or null when everything exists
    private String loadRoomAndFields(Physician from, Physician target) {

        ConsultingRoom room = null;
        if (from.getConsultingRoom() != null) {
            Optional<ConsultingRoom> found = consultingRoomRepository.findById(from.getConsultingRoom().getId());
            if (found.isEmpty()) {
                return "The consulting room with that id does not exist";
            }
            room = found.get();
        }

        List<MedicalField> fields = new ArrayList<>();
        if (from.getMedicalFields() != null) {
            for (MedicalField ref : from.getMedicalFields()) {
                Optional<MedicalField> found = medicalFieldRepository.findById(ref.getId());
                if (found.isEmpty()) {
                    return "That medical field does not exist";
                }
                fields.add(found.get());
            }
        }

        target.setConsultingRoom(room);
        target.setMedicalFields(fields);
        return null;
    }
}
