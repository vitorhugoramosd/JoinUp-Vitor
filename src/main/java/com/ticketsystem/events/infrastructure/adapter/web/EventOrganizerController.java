package com.ticketsystem.events.infrastructure.adapter.web;

import com.ticketsystem.events.application.dto.CreateEventRequestDTO;
import com.ticketsystem.events.application.dto.EventDTO;
import com.ticketsystem.events.application.usecase.CreateEventUseCase;
import com.ticketsystem.shared.infrastructure.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/organizer")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventOrganizerController {

    private final CreateEventUseCase createEventUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody CreateEventRequestDTO request,
                                                @RequestHeader("Authorization") String authorization) {
        Long organizerId = getUserIdFromToken(authorization);
        EventDTO event = createEventUseCase.execute(request, organizerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping("/{organizerId}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<EventDTO>> getOrganizerEvents(@PathVariable Long organizerId) {
        // TODO: Implement if needed
        return ResponseEntity.ok(List.of());
    }

    private Long getUserIdFromToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            return jwtTokenProvider.getUserIdFromToken(token);
        }
        throw new IllegalStateException("Token não encontrado");
    }
}

