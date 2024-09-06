package com.interventionManager.repositories;

import com.interventionManager.entities.MaterialHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialHistoryRepository extends JpaRepository<MaterialHistory, Long> {
}
