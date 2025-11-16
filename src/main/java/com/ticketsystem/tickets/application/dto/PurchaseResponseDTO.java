package com.ticketsystem.tickets.application.dto;

import com.ticketsystem.tickets.domain.model.Purchase;
import com.ticketsystem.tickets.domain.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDTO {
    private Long id;
    private Long userId;
    private Long eventId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String status;
    private String purchaseCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TicketResponseDTO> tickets;

    public static PurchaseResponseDTO fromDomain(Purchase purchase, List<Ticket> tickets) {
        return PurchaseResponseDTO.builder()
                .id(purchase.getId())
                .userId(purchase.getUserId())
                .eventId(purchase.getEventId())
                .quantity(purchase.getQuantity())
                .totalAmount(purchase.getTotalAmount())
                .status(purchase.getStatus().name())
                .purchaseCode(purchase.getPurchaseCode())
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .tickets(tickets.stream()
                        .map(TicketResponseDTO::fromDomain)
                        .collect(Collectors.toList()))
                .build();
    }
}

