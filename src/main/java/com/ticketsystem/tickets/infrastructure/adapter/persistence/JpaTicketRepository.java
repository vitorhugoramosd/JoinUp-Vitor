package com.ticketsystem.tickets.infrastructure.adapter.persistence;

import com.ticketsystem.tickets.domain.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketCode(String ticketCode);
    List<Ticket> findByPurchaseId(Long purchaseId);
    List<Ticket> findByEventId(Long eventId);
    long countByEventId(Long eventId);
}

