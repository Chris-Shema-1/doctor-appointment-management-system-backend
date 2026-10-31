package com.auca.doctorappointment.repository;

import com.auca.doctorappointment.entity.Consultation;
import com.auca.doctorappointment.entity.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

    // B2: consultations in one status, earliest consultDate first
    List<Consultation> findByStatusOrderByConsultDateAsc(ConsultationStatus status);

    // B3: consultations between two dates (Between includes both ends), ordered by consultDate
    List<Consultation> findByConsultDateBetweenOrderByConsultDateAsc(LocalDate start, LocalDate end);
}
