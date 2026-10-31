package com.auca.doctorappointment.repository;

import com.auca.doctorappointment.entity.Outpatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutpatientRepository extends JpaRepository<Outpatient, UUID> {

    // B1: outpatients with that lastName (any letter case), sorted by firstName A to Z
    List<Outpatient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);
}
