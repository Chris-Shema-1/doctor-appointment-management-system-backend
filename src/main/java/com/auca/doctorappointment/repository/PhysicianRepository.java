package com.auca.doctorappointment.repository;

import com.auca.doctorappointment.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhysicianRepository extends JpaRepository<Physician, UUID> {

    // C1: physicians holding that medical field (any letter case); medicalFields is a collection, so it needs a JOIN
    @Query("SELECT p FROM Physician p JOIN p.medicalFields m WHERE LOWER(m.name) = LOWER(:name)")
    List<Physician> findByMedicalFieldName(@Param("name") String name);
}
