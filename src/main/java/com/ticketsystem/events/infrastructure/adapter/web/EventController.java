package com.ticketsystem.events.infrastructure.adapter.web;

import com.ticketsystem.events.application.dto.EventDTO;
import com.ticketsystem.events.application.usecase.GetEventByIdUseCase;
import com.ticketsystem.events.application.usecase.ListEventsUseCase;
import com.ticketsystem.events.application.usecase.SearchEventsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventController {

    private final ListEventsUseCase listEventsUseCase;
    private final GetEventByIdUseCase getEventByIdUseCase;
    private final SearchEventsUseCase searchEventsUseCase;

    @GetMapping
    public ResponseEntity<List<EventDTO>> listAllEvents() {
        List<EventDTO> events = listEventsUseCase.execute();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO event = getEventByIdUseCase.execute(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestParam(value = "q", required = false) String searchTerm) {
        List<EventDTO> events = searchEventsUseCase.execute(searchTerm);
        return ResponseEntity.ok(events);
    }
}

