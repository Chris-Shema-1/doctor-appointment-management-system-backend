package com.auca.doctorappointment.service;

import com.auca.doctorappointment.entity.ConsultingRoom;
import com.auca.doctorappointment.entity.MedicalField;
import com.auca.doctorappointment.entity.Physician;
import com.auca.doctorappointment.repository.ConsultingRoomRepository;
import com.auca.doctorappointment.repository.MedicalFieldRepository;
import com.auca.doctorappointment.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String delete(UUID id) {
        Optional<Physician> physician = physicianRepository.findById(id);
        if (physician.isEmpty()) {
            return "The physician with that id does not exist";
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
