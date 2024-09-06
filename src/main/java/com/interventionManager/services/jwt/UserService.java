package com.interventionManager.services.jwt;

import com.interventionManager.dto.SignupRequest;
import com.interventionManager.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    public UserDetailsService UserDetailsService();
    UserDto getUserById(Long Id);
    UserDto updateUser(Long userId, SignupRequest signupRequest);
    //Long getUserIdByUsername(String username);
}
