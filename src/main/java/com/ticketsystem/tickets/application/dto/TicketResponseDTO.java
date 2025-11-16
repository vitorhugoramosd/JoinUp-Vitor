package com.ticketsystem.tickets.application.dto;

import com.ticketsystem.tickets.domain.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private Long eventId;
    private Long purchaseId;
    private String attendeeName;
    private String attendeeCpf;
    private String attendeeEmail;
    private LocalDate attendeeBirthDate;
    private BigDecimal price;
    private String status;
    private String ticketCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TicketResponseDTO fromDomain(Ticket ticket) {
        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .eventId(ticket.getEventId())
                .purchaseId(ticket.getPurchaseId())
                .attendeeName(ticket.getAttendeeName())
                .attendeeCpf(ticket.getAttendeeCpf())
                .attendeeEmail(ticket.getAttendeeEmail())
                .attendeeBirthDate(ticket.getAttendeeBirthDate())
                .price(ticket.getPrice())
                .status(ticket.getStatus().name())
                .ticketCode(ticket.getTicketCode())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}

