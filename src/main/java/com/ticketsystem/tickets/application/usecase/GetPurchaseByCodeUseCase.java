package com.ticketsystem.tickets.application.usecase;

import com.ticketsystem.tickets.application.dto.PurchaseResponseDTO;
import com.ticketsystem.tickets.domain.model.Purchase;
import com.ticketsystem.tickets.domain.model.Ticket;
import com.ticketsystem.tickets.domain.port.out.PurchaseRepository;
import com.ticketsystem.tickets.domain.port.out.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GetPurchaseByCodeUseCase {

    private final PurchaseRepository purchaseRepository;
    private final TicketRepository ticketRepository;

    public PurchaseResponseDTO execute(String purchaseCode) {
        Purchase purchase = purchaseRepository.findByPurchaseCode(purchaseCode)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Compra não encontrada"));

        List<Ticket> tickets = ticketRepository.findByPurchaseId(purchase.getId());
        return PurchaseResponseDTO.fromDomain(purchase, tickets);
    }
}

