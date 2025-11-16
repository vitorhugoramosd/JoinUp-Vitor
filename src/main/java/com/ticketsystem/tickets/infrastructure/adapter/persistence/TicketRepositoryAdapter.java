package com.ticketsystem.tickets.infrastructure.adapter.persistence;

import com.ticketsystem.tickets.domain.model.Ticket;
import com.ticketsystem.tickets.domain.port.out.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;

    @Override
    public Ticket save(Ticket ticket) {
        return jpaTicketRepository.save(ticket);
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return jpaTicketRepository.findById(id);
    }

    @Override
    public Optional<Ticket> findByTicketCode(String ticketCode) {
        return jpaTicketRepository.findByTicketCode(ticketCode);
    }

    @Override
    public List<Ticket> findByPurchaseId(Long purchaseId) {
        return jpaTicketRepository.findByPurchaseId(purchaseId);
    }

    @Override
    public List<Ticket> findByEventId(Long eventId) {
        return jpaTicketRepository.findByEventId(eventId);
    }

    @Override
    public long countByEventId(Long eventId) {
        return jpaTicketRepository.countByEventId(eventId);
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> tickets) {
        return jpaTicketRepository.saveAll(tickets);
    }

    @Override
    public void deleteById(Long id) {
        jpaTicketRepository.deleteById(id);
    }
}

