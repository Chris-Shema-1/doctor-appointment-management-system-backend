package com.auca.doctorappointment.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

// written only after the consultation is ATTENDED; Consultation has no List<LabTest> (one-way link)
@Entity
@Table(name = "lab_test")
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "findings")
    private String findings;

    @Column(name = "turnaround_hours")
    private int turnaroundHours;

    @Column(name = "ordered_date")
    private LocalDate orderedDate;

    // one consultation can have many lab tests
    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public int getTurnaroundHours() {
        return turnaroundHours;
    }

    public void setTurnaroundHours(int turnaroundHours) {
        this.turnaroundHours = turnaroundHours;
    }

    public LocalDate getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(LocalDate orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }
}
