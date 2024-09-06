package com.interventionManager.repositories;

import com.interventionManager.entities.RequestHistory;
import com.interventionManager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory, Long> {
    List<RequestHistory> findAllByTechnicianId(Long TechnicianId);
    List<RequestHistory> findAllByTitleContaining(String title);
    //List<RequestHistory> findByTechnicianIdAndAndArchiveDateBetween(Long TechnicianId, LocalDate startDate, LocalDate endDate);
}
