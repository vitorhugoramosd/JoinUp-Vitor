package com.ticketsystem.organizers.infrastructure.adapter.persistence;

import com.ticketsystem.organizers.domain.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaOrganizerRepository extends JpaRepository<Organizer, Long> {
    Optional<Organizer> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}

