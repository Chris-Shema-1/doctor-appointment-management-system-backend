package com.auca.doctorappointment.repository;

import com.auca.doctorappointment.entity.ConsultingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsultingRoomRepository extends JpaRepository<ConsultingRoom, UUID> {

    // D4: the room with that roomNumber, if any
    Optional<ConsultingRoom> findByRoomNumber(int roomNumber);

    // C2: rooms that no physician occupies, sorted by roomNumber
    @Query("SELECT r FROM ConsultingRoom r WHERE NOT EXISTS (SELECT p FROM Physician p WHERE p.consultingRoom = r) ORDER BY r.roomNumber")
    List<ConsultingRoom> findFreeRooms();
}
