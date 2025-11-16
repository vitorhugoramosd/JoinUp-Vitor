package com.ticketsystem.events.application.dto;

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
public class EventWithMetricsDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private BigDecimal ticketPrice;
    private Integer totalTickets;
    private Integer availableTickets;
    private Long ticketsSold;
    private BigDecimal totalRevenue;
    private Long totalPurchases;
    private Integer ticketsRemaining;
    private BigDecimal occupancyRate;
}

