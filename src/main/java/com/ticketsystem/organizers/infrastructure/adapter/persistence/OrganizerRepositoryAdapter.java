package com.ticketsystem.organizers.infrastructure.adapter.persistence;

import com.ticketsystem.organizers.domain.model.Organizer;
import com.ticketsystem.organizers.domain.port.out.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrganizerRepositoryAdapter implements OrganizerRepository {

    private final JpaOrganizerRepository jpaOrganizerRepository;

    @Override
    public Organizer save(Organizer organizer) {
        return jpaOrganizerRepository.save(organizer);
    }

    @Override
    public Optional<Organizer> findById(Long id) {
        return jpaOrganizerRepository.findById(id);
    }

    @Override
    public Optional<Organizer> findByUserId(Long userId) {
        return jpaOrganizerRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return jpaOrganizerRepository.existsByUserId(userId);
    }
}

