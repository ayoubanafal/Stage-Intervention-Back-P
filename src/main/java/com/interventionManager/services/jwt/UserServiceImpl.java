package com.interventionManager.services.jwt;

import com.interventionManager.dto.SignupRequest;
import com.interventionManager.dto.UserDto;
import com.interventionManager.entities.Request;
import com.interventionManager.entities.User;
import com.interventionManager.enums.UserRole;
import com.interventionManager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

private final UserRepository userRepository;


    @Override
    public UserDetailsService UserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findFirstByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(User::getUserDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDto updateUser(Long userId, SignupRequest signupRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (signupRequest.getName() != null) {
                user.setName(signupRequest.getName());
            }
            if (signupRequest.getPhone() != null) {
                user.setPhone(signupRequest.getPhone());
            }
            if (signupRequest.getPosition() != null) {
                user.setPosition(signupRequest.getPosition());
            }
            if (signupRequest.getEmail() != null) {
                user.setEmail(signupRequest.getEmail());
            }
            if (signupRequest.getPassword() != null) {
                user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
            }
            return userRepository.save(user).getUserDto();
        }
        return null;
    }
//    @Override
//    public Long getUserIdByUsername(String username){
//        Optional<User> user = userRepository.findUsersByName(username);
//        return user.get().getUserId();
//    }

}
