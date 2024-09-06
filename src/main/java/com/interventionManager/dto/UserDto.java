package com.interventionManager.dto;

import com.interventionManager.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
    private String department;
    private String position;
    private String phone;
}
