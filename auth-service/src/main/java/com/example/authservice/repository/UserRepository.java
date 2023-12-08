package com.example.authservice.repository;

import com.example.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String userName);
    Boolean existsByLogin(String username);
    Boolean existsByEmail(String email);
}
