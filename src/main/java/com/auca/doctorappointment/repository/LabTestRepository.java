package com.auca.doctorappointment.repository;

import com.auca.doctorappointment.entity.Consultation;
import com.auca.doctorappointment.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, UUID> {

    // E2: every lab test of one outpatient, newest orderedDate first;
    // consultation and outpatient are single-valued, so we navigate with dots (no JOIN)
    @Query("SELECT d FROM LabTest d WHERE d.consultation.outpatient.nationalId = :nationalId ORDER BY d.orderedDate DESC")
    List<LabTest> findByOutpatientNationalId(@Param("nationalId") String nationalId);

    // E3: does this consultation have at least one lab test?
    boolean existsByConsultation(Consultation consultation);
}
