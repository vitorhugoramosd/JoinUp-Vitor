package com.ticketsystem.events.application.dto;

import jakarta.validation.constraints.*;
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
public class CreateEventRequestDTO {
    @NotBlank(message = "Nome do evento é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    private String name;

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String description;

    @NotNull(message = "Data do evento é obrigatória")
    @Future(message = "Data do evento deve ser no futuro")
    private LocalDateTime eventDate;

    @NotBlank(message = "Local do evento é obrigatório")
    @Size(max = 255, message = "Local deve ter no máximo 255 caracteres")
    private String location;

    @NotNull(message = "Preço do ingresso é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Preço inválido")
    private BigDecimal ticketPrice;

    @NotNull(message = "Quantidade total de ingressos é obrigatória")
    @Min(value = 1, message = "Deve haver pelo menos 1 ingresso")
    private Integer totalTickets;
}

