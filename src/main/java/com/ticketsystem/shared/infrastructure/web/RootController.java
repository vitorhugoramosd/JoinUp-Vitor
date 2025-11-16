package com.ticketsystem.shared.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sistema de Compra de Ingressos - API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("endpoints", Map.of(
            "public", "/api/events, /api/users/register, /api/auth/login",
            "protected", "/api/tickets/** (requires authentication)",
            "organizer", "/api/events/organizer, /api/dashboard/** (requires ORGANIZER role)",
            "h2-console", "/h2-console (for database access)"
        ));
        return ResponseEntity.ok(response);
    }
}

