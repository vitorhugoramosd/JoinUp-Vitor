package com.ticketsystem.tickets.infrastructure.adapter.persistence;

import com.ticketsystem.tickets.domain.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findByPurchaseCode(String purchaseCode);
    List<Purchase> findByUserId(Long userId);
    List<Purchase> findByEventId(Long eventId);
    long countByEventId(Long eventId);
    
    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Purchase p WHERE p.eventId = :eventId AND p.status = 'CONFIRMED'")
    long countConfirmedTicketsByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Purchase p WHERE p.eventId = :eventId AND p.status = 'CONFIRMED'")
    BigDecimal sumTotalAmountByEventId(@Param("eventId") Long eventId);
}

