package com.ticketsystem.users.infrastructure.adapter.persistence;

import com.ticketsystem.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
    boolean existsByEmail(String email);
}

