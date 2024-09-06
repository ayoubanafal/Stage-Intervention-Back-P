package com.interventionManager.repositories;

import com.interventionManager.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByNameContaining(String title);
}
