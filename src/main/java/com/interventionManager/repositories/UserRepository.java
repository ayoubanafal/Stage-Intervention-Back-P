package com.interventionManager.repositories;

import com.interventionManager.dto.UserDto;
import com.interventionManager.entities.User;
import com.interventionManager.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String username);
    Optional<User> findByUserRole(UserRole userRole);
    List<User> findAllByNameContaining(String name);
    List<User> findUsersByNameContaining(String name);
    //Optional<User> findUsersByName(String name);
    User findByEmail(String email);
}
