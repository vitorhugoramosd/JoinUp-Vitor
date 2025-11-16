package com.ticketsystem.tickets.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestDTO {
    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @NotNull(message = "ID do evento é obrigatório")
    private Long eventId;

    @NotEmpty(message = "Lista de participantes é obrigatória")
    @Valid
    private List<AttendeeDTO> attendees;
}

