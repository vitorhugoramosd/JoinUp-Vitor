package com.ticketsystem.organizers.infrastructure.adapter.persistence;

import com.ticketsystem.organizers.domain.model.Organizer;
import com.ticketsystem.organizers.domain.port.out.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaOrganizerRepository extends JpaRepository<Organizer, Long> {
    Optional<Organizer> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}

@Component
@RequiredArgsConstructor
class OrganizerRepositoryAdapter implements OrganizerRepository {

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

