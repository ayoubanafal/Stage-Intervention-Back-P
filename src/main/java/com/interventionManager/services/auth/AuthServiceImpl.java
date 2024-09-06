package com.interventionManager.services.auth;

import com.interventionManager.dto.SignupRequest;
import com.interventionManager.dto.UserDto;
import com.interventionManager.entities.User;
import com.interventionManager.enums.UserRole;
import com.interventionManager.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;

//    @PostConstruct
//    private void createAnAdminAccount(){
//        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.Admin);
//        if (optionalUser.isEmpty())
//        {
//            User user= new User();
//            user.setEmail("admin@test.com");
//            user.setName("admin");
//            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
//            user.setUserRole(UserRole.Admin);
//            userRepository.save(user);
//            System.out.println("Admin Account Created Successfully !");
//        }
//        else {
//            System.out.println("Admin Account Already exists !!");
//        }
//
//
//        Optional<User> optionalUser3 = userRepository.findByUserRole(UserRole.Technician);
//        if (optionalUser3.isEmpty())
//        {
//            User user= new User();
//            user.setEmail("technician@test.com");
//            user.setName("technician");
//            user.setPassword(new BCryptPasswordEncoder().encode("technician"));
//            user.setUserRole(UserRole.Technician);
//            //user.setDepartment("IT Departement");
//            //user.setPosition("Big Boss");
//            //user.setPhone("619");
//            userRepository.save(user);
//            System.out.println("technician Account Created Successfully !");
//        }
//        else {
//            System.out.println("technician Account Already exists !!");
//        }
//    }

    @Override
    public UserDto signupUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.Requester);
        //user.setDepartment(signupRequest.getDepartment());
        user.setPosition(signupRequest.getPosition());
        user.setPhone(signupRequest.getPhone());
        User createdUser=userRepository.save(user);
        return createdUser.getUserDto();
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
