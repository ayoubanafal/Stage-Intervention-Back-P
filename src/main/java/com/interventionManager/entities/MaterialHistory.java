package com.interventionManager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class MaterialHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long archivedMaterialId;
    private Long materialId;
    private String name;
    private String detailedDescription;
    private Date integrationDate;
    private String lifeSpan; // in years
    private String location;
    private String status;
    private String deletionReason;
    private Date deletionDate;
    public MaterialHistory() {}

    public MaterialHistory(Material material,String deletionReason) {
        this.materialId = material.getMaterialId();
        this.name = material.getName();
        this.detailedDescription = material.getDetailedDescription();
        this.integrationDate = material.getIntegrationDate();
        this.lifeSpan = material.getLifeSpan();
        this.location = material.getLocation();
        this.status = material.getStatus();
        this.deletionReason = deletionReason;
        this.deletionDate = new Date();
    }
}
