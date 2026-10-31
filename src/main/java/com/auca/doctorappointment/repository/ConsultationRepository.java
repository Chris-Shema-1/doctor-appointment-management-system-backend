package com.auca.doctorappointment.repository;

import com.auca.doctorappointment.entity.Consultation;
import com.auca.doctorappointment.entity.ConsultationStatus;
import com.auca.doctorappointment.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // D1: does the physician already have a consultation on that date whose status is not the given one (CANCELLED)?
    boolean existsByPhysicianAndConsultDateAndStatusNot(Physician physician, LocalDate consultDate,
            ConsultationStatus status);

    // Bonus dashboard: derived counts
    long countByConsultDate(LocalDate consultDate);

    long countByStatus(ConsultationStatus status);

    // F1: one row per status with its count, e.g. ["BOOKED", 4]; a status with no consultations has no group, so no row
    @Query("SELECT c.status, COUNT(c) FROM Consultation c GROUP BY c.status")
    List<Object[]> countPerStatus();

    // F2: one UPDATE for the whole day, no Java loop; ATTENDED ones are kept, and already CANCELLED ones are
    // left out so the returned int is the number of consultations this call really cancelled
    @Modifying
    @Query("""
            UPDATE Consultation c SET c.status = com.auca.doctorappointment.entity.ConsultationStatus.CANCELLED
            WHERE c.physician.id = :physicianId AND c.consultDate = :date
            AND c.status <> com.auca.doctorappointment.entity.ConsultationStatus.ATTENDED
            AND c.status <> com.auca.doctorappointment.entity.ConsultationStatus.CANCELLED
            """)
    int cancelDay(@Param("physicianId") UUID physicianId, @Param("date") LocalDate date);
}
