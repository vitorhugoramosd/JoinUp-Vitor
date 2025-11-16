package com.ticketsystem.events.application.dto;

import com.ticketsystem.events.domain.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private BigDecimal ticketPrice;
    private Integer totalTickets;
    private Integer availableTickets;
    private Long organizerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EventDTO fromDomain(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .ticketPrice(event.getTicketPrice())
                .totalTickets(event.getTotalTickets())
                .availableTickets(event.getAvailableTickets())
                .organizerId(event.getOrganizerId())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}

