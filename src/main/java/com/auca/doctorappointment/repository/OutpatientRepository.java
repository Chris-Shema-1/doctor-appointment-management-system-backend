package com.auca.doctorappointment.repository;

import com.auca.doctorappointment.entity.Outpatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutpatientRepository extends JpaRepository<Outpatient, UUID> {

    // B1: outpatients with that lastName (any letter case), sorted by firstName A to Z
    List<Outpatient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // C4: outpatients with at least one consultation with that physician, each listed once (DISTINCT);
    // c.outpatient and c.physician are single-valued, so a dot is enough (no JOIN)
    @Query("SELECT DISTINCT c.outpatient FROM Consultation c WHERE c.physician.id = :physicianId")
    List<Outpatient> findByPhysicianId(@Param("physicianId") UUID physicianId);
}
