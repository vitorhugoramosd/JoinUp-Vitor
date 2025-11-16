package com.ticketsystem.tickets.application.usecase;

import com.ticketsystem.tickets.application.dto.PurchaseResponseDTO;
import com.ticketsystem.tickets.domain.model.Purchase;
import com.ticketsystem.tickets.domain.model.Ticket;
import com.ticketsystem.tickets.domain.port.out.PurchaseRepository;
import com.ticketsystem.tickets.domain.port.out.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListUserPurchasesUseCase {

    private final PurchaseRepository purchaseRepository;
    private final TicketRepository ticketRepository;

    public List<PurchaseResponseDTO> execute(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByUserId(userId);

        return purchases.stream()
                .map(purchase -> {
                    List<Ticket> tickets = ticketRepository.findByPurchaseId(purchase.getId());
                    return PurchaseResponseDTO.fromDomain(purchase, tickets);
                })
                .collect(Collectors.toList());
    }
}

