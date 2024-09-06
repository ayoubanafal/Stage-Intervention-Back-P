package com.interventionManager.repositories;

import com.interventionManager.dto.RequestDto;
import com.interventionManager.entities.Request;
import com.interventionManager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByTitleContaining(String title);
    List<Request> findAllByTechnician(Optional<User> technician);
    List<Request> findByTechnicianAndAndCreationDateBetween(Optional<User> technician, LocalDate fromDate, LocalDate toDate);
    //long countByTechnician(Optional<User> user);
   // long countByTechnicianAndStatus(Long technicianId, String status);
}
