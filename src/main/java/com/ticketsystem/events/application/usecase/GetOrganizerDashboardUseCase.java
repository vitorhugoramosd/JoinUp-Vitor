package com.ticketsystem.events.application.usecase;

import com.ticketsystem.events.application.dto.EventWithMetricsDTO;
import com.ticketsystem.events.domain.model.Event;
import com.ticketsystem.events.domain.port.out.EventRepository;
import com.ticketsystem.tickets.domain.port.out.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOrganizerDashboardUseCase {

    private final EventRepository eventRepository;
    private final PurchaseRepository purchaseRepository;

    public List<EventWithMetricsDTO> execute(Long organizerId) {
        List<Event> events = eventRepository.findByOrganizerId(organizerId);

        return events.stream()
                .map(event -> {
                    // Calculate tickets sold from confirmed purchases
                    // For now, we count purchases as tickets sold (simplified)
                    // In a real system, we'd sum the quantity from all confirmed purchases
                    long totalPurchases = purchaseRepository.countByEventId(event.getId());
                    long ticketsSold = event.getTotalTickets() - event.getAvailableTickets();
                    BigDecimal totalRevenue = purchaseRepository.sumTotalAmountByEventId(event.getId());

                    double occupancyRate = event.getTotalTickets() > 0
                            ? (double) ticketsSold / event.getTotalTickets() * 100
                            : 0.0;

                    return EventWithMetricsDTO.builder()
                            .id(event.getId())
                            .name(event.getName())
                            .description(event.getDescription())
                            .eventDate(event.getEventDate())
                            .location(event.getLocation())
                            .ticketPrice(event.getTicketPrice())
                            .totalTickets(event.getTotalTickets())
                            .availableTickets(event.getAvailableTickets())
                            .ticketsSold(ticketsSold)
                            .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                            .totalPurchases(totalPurchases)
                            .ticketsRemaining(event.getAvailableTickets())
                            .occupancyRate(BigDecimal.valueOf(occupancyRate).setScale(2, RoundingMode.HALF_UP))
                            .build();
                })
                .collect(Collectors.toList());
    }
}

