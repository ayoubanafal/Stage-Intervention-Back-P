package com.interventionManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interventionManager.dto.RequestDto;
import com.interventionManager.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Data
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private User requester;
    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    @JoinColumn(name = "technician_id", nullable = true )
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private User technician;

    /*@ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "userId")
    private Material material;*/

    private String description;
    private String title;
    private String priority;
    private RequestStatus status;
    private Date creationDate;
    private Date lastUpdate;

    public RequestDto getRequestDto() {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestId(requestId);
        requestDto.setDescription(description);
        requestDto.setTitle(title);
        requestDto.setPriority(priority);
        requestDto.setStatus(status);
        requestDto.setCreationDate(creationDate);
        requestDto.setLastUpdate(lastUpdate);
        requestDto.setEmployeeName(requester.getName());
        requestDto.setRequesterId(requester.getUserId());
        requestDto.setTechnicianId(technician.getUserId());
        return requestDto;
    }
}
