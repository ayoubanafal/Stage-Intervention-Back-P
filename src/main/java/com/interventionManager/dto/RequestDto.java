package com.interventionManager.dto;

import com.interventionManager.entities.User;
import com.interventionManager.enums.RequestStatus;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Data
public class RequestDto {
    private Long requestId;
    private Long requesterId;
    private Long technicianId;
    private String employeeName;

    private String description;
    private String title;
    private String priority;
    private RequestStatus status;
    private Date creationDate;
    private Date lastUpdate;//aka due date
}
