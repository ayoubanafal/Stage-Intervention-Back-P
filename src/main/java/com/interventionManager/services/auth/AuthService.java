package com.interventionManager.services.auth;

import com.interventionManager.dto.SignupRequest;
import com.interventionManager.dto.UserDto;

public interface AuthService {
    UserDto signupUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
}
