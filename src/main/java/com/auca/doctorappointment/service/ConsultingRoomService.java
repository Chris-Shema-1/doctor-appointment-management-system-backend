package com.auca.doctorappointment.service;

import com.auca.doctorappointment.entity.ConsultingRoom;
import com.auca.doctorappointment.repository.ConsultingRoomRepository;
import com.auca.doctorappointment.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultingRoomService {

    @Autowired
    private ConsultingRoomRepository consultingRoomRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    public String save(ConsultingRoom consultingRoom) {
        consultingRoom.setId(null);
        consultingRoomRepository.save(consultingRoom);
        return "Consulting room saved successfully";
    }

    public List<ConsultingRoom> getAll() {
        return consultingRoomRepository.findAll();
    }

    // Optional.empty() means the consulting room does not exist
    public Optional<ConsultingRoom> getById(UUID id) {
        return consultingRoomRepository.findById(id);
    }

    // C2
    public List<ConsultingRoom> getFreeRooms() {
        return consultingRoomRepository.findFreeRooms();
    }

    public String update(UUID id, ConsultingRoom newData) {
        Optional<ConsultingRoom> consultingRoom = consultingRoomRepository.findById(id);
        if (consultingRoom.isEmpty()) {
            return "The consulting room with that id does not exist";
        }
        consultingRoom.get().setRoomNumber(newData.getRoomNumber());
        consultingRoom.get().setName(newData.getName());
        consultingRoomRepository.save(consultingRoom.get());
        return "Consulting room updated successfully";
    }

    public String delete(UUID id) {
        Optional<ConsultingRoom> consultingRoom = consultingRoomRepository.findById(id);
        if (consultingRoom.isEmpty()) {
            return "The consulting room with that id does not exist";
        }
        // a physician's consulting_room_id still points at this room; deleting it would fail (reuses D4's method)
        if (physicianRepository.existsByConsultingRoom(consultingRoom.get())) {
            return "That consulting room is taken by a physician, move the physician first";
        }
        consultingRoomRepository.delete(consultingRoom.get());
        return "Consulting room deleted successfully";
    }
}
