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
public class ListEventsUseCase {

    private final EventRepository eventRepository;

    public List<EventDTO> execute() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(EventDTO::fromDomain)
                .collect(Collectors.toList());
    }
}

