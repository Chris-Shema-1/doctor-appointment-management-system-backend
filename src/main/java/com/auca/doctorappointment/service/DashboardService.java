package com.auca.doctorappointment.service;

import com.auca.doctorappointment.entity.ConsultationStatus;
import com.auca.doctorappointment.repository.ConsultationRepository;
import com.auca.doctorappointment.repository.ConsultingRoomRepository;
import com.auca.doctorappointment.repository.LabTestRepository;
import com.auca.doctorappointment.repository.OutpatientRepository;
import com.auca.doctorappointment.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private OutpatientRepository outpatientRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private ConsultingRoomRepository consultingRoomRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private LabTestRepository labTestRepository;

    // Bonus: one JSON object with the numbers of the system; LinkedHashMap keeps the keys in this order
    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("outpatients", outpatientRepository.count());
        summary.put("physicians", physicianRepository.count());
        summary.put("freeRooms", (long) consultingRoomRepository.findFreeRooms().size()); // reuses C2
        summary.put("consultationsToday", consultationRepository.countByConsultDate(LocalDate.now()));
        summary.put("booked", consultationRepository.countByStatus(ConsultationStatus.BOOKED));
        summary.put("labTests", labTestRepository.count());
        return summary;
    }
}
