package com.ticketsystem.events.application.usecase;

import com.ticketsystem.events.application.dto.EventDTO;
import com.ticketsystem.events.domain.model.Event;
import com.ticketsystem.events.domain.port.out.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GetEventByIdUseCase {

    private final EventRepository eventRepository;

    public EventDTO execute(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Evento não encontrado"));
        return EventDTO.fromDomain(event);
    }
}

