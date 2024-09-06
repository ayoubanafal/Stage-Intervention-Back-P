package com.interventionManager.entities;

import com.interventionManager.enums.RequestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class RequestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    private Long originalRequestId;

    private String description;
    private String title;
    private String priority;
    private RequestStatus status;
    private Date creationDate;
    private Date lastUpdate;
    private Date archiveDate;

    private String requesterName;
    private Long requesterId;

    private String technicianName;
    private Long technicianId;

    public RequestHistory() {
    }

    public RequestHistory(Request request) {
        this.originalRequestId = request.getRequestId();
        this.description = request.getDescription();
        this.title = request.getTitle();
        this.priority = request.getPriority();
        this.status = request.getStatus();
        this.creationDate = request.getCreationDate();
        this.lastUpdate = request.getLastUpdate();
        this.archiveDate = new Date();

        this.requesterName = request.getRequester().getName();
        this.requesterId = request.getRequester().getUserId();

        if (request.getTechnician() != null) {
            this.technicianName = request.getTechnician().getName();
            this.technicianId = request.getTechnician().getUserId();
        }
    }
}
