package com.ticketsystem.events.infrastructure.adapter.persistence;

import com.ticketsystem.events.domain.model.Event;
import com.ticketsystem.events.domain.port.out.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepository {

    private final JpaEventRepository jpaEventRepository;

    @Override
    public Event save(Event event) {
        return jpaEventRepository.save(event);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return jpaEventRepository.findById(id);
    }

    @Override
    public List<Event> findAll() {
        return jpaEventRepository.findAll();
    }

    @Override
    public List<Event> findByNameContainingIgnoreCase(String name) {
        return jpaEventRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Event> findByOrganizerId(Long organizerId) {
        return jpaEventRepository.findByOrganizerId(organizerId);
    }

    @Override
    public void deleteById(Long id) {
        jpaEventRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaEventRepository.existsById(id);
    }
}

