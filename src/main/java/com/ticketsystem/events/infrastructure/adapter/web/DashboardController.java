package com.ticketsystem.events.infrastructure.adapter.web;

import com.ticketsystem.events.application.dto.EventWithMetricsDTO;
import com.ticketsystem.events.application.usecase.GetOrganizerDashboardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final GetOrganizerDashboardUseCase getOrganizerDashboardUseCase;

    @GetMapping("/organizer/{organizerId}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<EventWithMetricsDTO>> getOrganizerDashboard(@PathVariable Long organizerId) {
        List<EventWithMetricsDTO> dashboard = getOrganizerDashboardUseCase.execute(organizerId);
        return ResponseEntity.ok(dashboard);
    }
}

