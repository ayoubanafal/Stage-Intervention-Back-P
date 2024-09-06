package com.interventionManager.repositories;

import com.interventionManager.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
      List<Chat> findByTechnicianIdAndAndTimestampAfter(String technicianId, LocalDateTime timestamp);
      List<Chat> findAllByTimestampBefore(LocalDateTime timestamp);
}
