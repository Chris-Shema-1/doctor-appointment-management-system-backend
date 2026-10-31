package com.auca.doctorappointment.controller;

import com.auca.doctorappointment.entity.ConsultingRoom;
import com.auca.doctorappointment.service.ConsultingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/consulting-rooms")
public class ConsultingRoomController {

    @Autowired
    private ConsultingRoomService consultingRoomService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ConsultingRoom consultingRoom) {
        return new ResponseEntity<>(consultingRoomService.save(consultingRoom), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ConsultingRoom>> getAll() {
        return new ResponseEntity<>(consultingRoomService.getAll(), HttpStatus.OK);
    }

    // C2: GET /api/consulting-rooms/free
    @GetMapping("/free")
    public ResponseEntity<List<ConsultingRoom>> getFreeRooms() {
        return new ResponseEntity<>(consultingRoomService.getFreeRooms(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<ConsultingRoom> consultingRoom = consultingRoomService.getById(id);
        if (consultingRoom.isEmpty()) {
            return new ResponseEntity<>("The consulting room with that id does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(consultingRoom.get(), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ConsultingRoom consultingRoom) {
        String message = consultingRoomService.update(id, consultingRoom);
        if (message.equals("The consulting room with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = consultingRoomService.delete(id);
        if (message.equals("The consulting room with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
