package com.interventionManager.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String department;
    private String position;
    private String phone;
}
