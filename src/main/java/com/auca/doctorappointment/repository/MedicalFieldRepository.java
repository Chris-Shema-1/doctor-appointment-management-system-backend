package com.auca.doctorappointment.repository;

import com.auca.doctorappointment.entity.MedicalField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicalFieldRepository extends JpaRepository<MedicalField, UUID> {
}
