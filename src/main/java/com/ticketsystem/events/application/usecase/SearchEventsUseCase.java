package com.ticketsystem.events.application.usecase;

import com.ticketsystem.events.application.dto.EventDTO;
import com.ticketsystem.events.domain.model.Event;
import com.ticketsystem.events.domain.port.out.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchEventsUseCase {

    private final EventRepository eventRepository;

    public List<EventDTO> execute(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return eventRepository.findAll().stream()
                    .map(EventDTO::fromDomain)
                    .collect(Collectors.toList());
        }

        List<Event> events = eventRepository.findByNameContainingIgnoreCase(searchTerm.trim());
        return events.stream()
                .map(EventDTO::fromDomain)
                .collect(Collectors.toList());
    }
}

