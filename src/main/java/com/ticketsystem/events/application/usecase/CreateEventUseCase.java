package com.ticketsystem.events.application.usecase;

import com.ticketsystem.events.application.dto.CreateEventRequestDTO;
import com.ticketsystem.events.application.dto.EventDTO;
import com.ticketsystem.events.domain.model.Event;
import com.ticketsystem.events.domain.port.out.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateEventUseCase {

    private final EventRepository eventRepository;

    @Transactional
    public EventDTO execute(CreateEventRequestDTO request, Long organizerId) {
        Event event = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .location(request.getLocation())
                .ticketPrice(request.getTicketPrice())
                .totalTickets(request.getTotalTickets())
                .availableTickets(request.getTotalTickets())
                .organizerId(organizerId)
                .build();

        Event savedEvent = eventRepository.save(event);
        return EventDTO.fromDomain(savedEvent);
    }
}

