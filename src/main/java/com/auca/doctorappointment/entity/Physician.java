package com.auca.doctorappointment.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "physician")
public class Physician {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "licence_number")
    private String licenceNumber;

    // may be null: a physician without a consulting room
    @ManyToOne
    @JoinColumn(name = "consulting_room_id")
    private ConsultingRoom consultingRoom;

    // owning side of the many-to-many: the link rows are stored from here
    @ManyToMany
    @JoinTable(
            name = "physician_medical_field",
            joinColumns = @JoinColumn(name = "physician_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_field_id")
    )
    private List<MedicalField> medicalFields = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public ConsultingRoom getConsultingRoom() {
        return consultingRoom;
    }

    public void setConsultingRoom(ConsultingRoom consultingRoom) {
        this.consultingRoom = consultingRoom;
    }

    public List<MedicalField> getMedicalFields() {
        return medicalFields;
    }

    public void setMedicalFields(List<MedicalField> medicalFields) {
        this.medicalFields = medicalFields;
    }
}
