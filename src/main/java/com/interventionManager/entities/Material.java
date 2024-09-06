package com.interventionManager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;

    private String name;
    private String detailedDescription;
    private Date integrationDate;
    private String lifeSpan; // in years
    private String location;
    private String status;

    public Material() {
        // No-arg constructor
    }

    public Material(String name, Date integrationDate, String lifeSpan, String location, String detailedDescription,String status) {
        this.name = name;
        this.integrationDate = integrationDate;
        this.lifeSpan = lifeSpan;
        this.location = location;
        this.detailedDescription = detailedDescription;
        this.status = status;
    }
}