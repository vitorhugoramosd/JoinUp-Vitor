package com.ticketsystem.tickets.infrastructure.adapter.persistence;

import com.ticketsystem.tickets.domain.model.Purchase;
import com.ticketsystem.tickets.domain.port.out.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PurchaseRepositoryAdapter implements PurchaseRepository {

    private final JpaPurchaseRepository jpaPurchaseRepository;

    @Override
    public Purchase save(Purchase purchase) {
        return jpaPurchaseRepository.save(purchase);
    }

    @Override
    public Optional<Purchase> findById(Long id) {
        return jpaPurchaseRepository.findById(id);
    }

    @Override
    public Optional<Purchase> findByPurchaseCode(String purchaseCode) {
        return jpaPurchaseRepository.findByPurchaseCode(purchaseCode);
    }

    @Override
    public List<Purchase> findByUserId(Long userId) {
        return jpaPurchaseRepository.findByUserId(userId);
    }

    @Override
    public List<Purchase> findByEventId(Long eventId) {
        return jpaPurchaseRepository.findByEventId(eventId);
    }

    @Override
    public long countByEventId(Long eventId) {
        return jpaPurchaseRepository.countByEventId(eventId);
    }

    @Override
    public BigDecimal sumTotalAmountByEventId(Long eventId) {
        return jpaPurchaseRepository.sumTotalAmountByEventId(eventId);
    }

    @Override
    public void deleteById(Long id) {
        jpaPurchaseRepository.deleteById(id);
    }
}

