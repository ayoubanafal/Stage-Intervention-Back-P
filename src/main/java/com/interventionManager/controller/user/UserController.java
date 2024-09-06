package com.interventionManager.controller.user;

import com.interventionManager.dto.SignupRequest;
import com.interventionManager.dto.UserDto;
import com.interventionManager.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> signupUser(@PathVariable Long id,@RequestBody SignupRequest signupRequest){
        UserDto createdUserDto = userService.updateUser(id,signupRequest);
        if (createdUserDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Updated");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }
}
