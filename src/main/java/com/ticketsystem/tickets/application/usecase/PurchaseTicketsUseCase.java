package com.ticketsystem.tickets.application.usecase;

import com.ticketsystem.events.domain.model.Event;
import com.ticketsystem.events.domain.port.out.EventRepository;
import com.ticketsystem.shared.infrastructure.email.EmailService;
import com.ticketsystem.tickets.application.dto.AttendeeDTO;
import com.ticketsystem.tickets.application.dto.PurchaseRequestDTO;
import com.ticketsystem.tickets.application.dto.PurchaseResponseDTO;
import com.ticketsystem.tickets.domain.model.Purchase;
import com.ticketsystem.tickets.domain.model.Ticket;
import com.ticketsystem.tickets.domain.port.out.PurchaseRepository;
import com.ticketsystem.tickets.domain.port.out.TicketRepository;
import com.ticketsystem.users.domain.model.User;
import com.ticketsystem.users.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseTicketsUseCase {

    private final PurchaseRepository purchaseRepository;
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public PurchaseResponseDTO execute(PurchaseRequestDTO request) {
        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado"));

        // Fetch event and validate availability
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Evento não encontrado"));

        int requestedQuantity = request.getAttendees().size();
        if (!event.hasAvailableTickets(requestedQuantity)) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    String.format("Ingressos insuficientes. Solicitado: %d, Disponível: %d",
                            requestedQuantity, event.getAvailableTickets())
            );
        }

        // Validate attendees count matches quantity
        if (request.getAttendees().size() != requestedQuantity) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Número de participantes deve ser igual à quantidade de ingressos"
            );
        }

        // Create purchase
        Purchase purchase = Purchase.builder()
                .userId(request.getUserId())
                .eventId(request.getEventId())
                .quantity(requestedQuantity)
                .status(Purchase.PurchaseStatus.PENDING)
                .build();

        // Calculate total amount
        purchase.calculateTotalAmount(event.getTicketPrice());

        // Reserve tickets in event
        event.reserveTickets(requestedQuantity);
        eventRepository.save(event);

        // Save purchase
        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Create tickets for each attendee
        List<Ticket> tickets = new ArrayList<>();
        for (AttendeeDTO attendeeDTO : request.getAttendees()) {
            Ticket ticket = Ticket.builder()
                    .purchaseId(savedPurchase.getId())
                    .eventId(request.getEventId())
                    .attendeeName(attendeeDTO.getFullName())
                    .attendeeCpf(attendeeDTO.getCpf())
                    .attendeeEmail(attendeeDTO.getEmail())
                    .attendeeBirthDate(attendeeDTO.getBirthDate())
                    .price(event.getTicketPrice())
                    .status(Ticket.TicketStatus.ACTIVE)
                    .build();

            tickets.add(ticket);
        }

        // Save tickets
        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);

        // Confirm purchase
        savedPurchase.confirm();
        purchaseRepository.save(savedPurchase);

        log.info("Purchase confirmed: {} for user {} and event {}",
                savedPurchase.getPurchaseCode(), user.getEmail(), event.getName());

        // Send confirmation email
        try {
            emailService.sendPurchaseConfirmationEmail(
                    user.getEmail(),
                    savedPurchase.getPurchaseCode(),
                    event.getName(),
                    requestedQuantity
            );
        } catch (Exception e) {
            log.error("Failed to send purchase confirmation email", e);
        }

        return PurchaseResponseDTO.fromDomain(savedPurchase, savedTickets);
    }
}

