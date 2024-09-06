package com.interventionManager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private Long userId;
    private Long requestId;
    private Date creationDate;
    private String text;

    public Comment(){}
    public Comment(Long userId, Long requestId, Date creationDate, String text){
        this.userId=userId;
        this.requestId=requestId;
        this.creationDate=creationDate;
        this.text=text;
    }
}
