package com.auca.doctorappointment.entity;

import jakarta.persistence.*;

import java.util.UUID;

// a consulting room knows nothing about its physician (no relationship field here)
@Entity
@Table(name = "consulting_room")
public class ConsultingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_number")
    private int roomNumber;

    @Column(name = "name")
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
