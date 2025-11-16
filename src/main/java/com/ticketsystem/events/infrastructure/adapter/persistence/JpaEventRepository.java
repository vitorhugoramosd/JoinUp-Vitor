package com.ticketsystem.events.infrastructure.adapter.persistence;

import com.ticketsystem.events.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaEventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByOrganizerId(Long organizerId);
}
