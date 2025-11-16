package com.ticketsystem.tickets.infrastructure.adapter.web;

import com.ticketsystem.tickets.application.dto.PurchaseRequestDTO;
import com.ticketsystem.tickets.application.dto.PurchaseResponseDTO;
import com.ticketsystem.tickets.application.usecase.GetPurchaseByCodeUseCase;
import com.ticketsystem.tickets.application.usecase.ListUserPurchasesUseCase;
import com.ticketsystem.tickets.application.usecase.PurchaseTicketsUseCase;
import com.ticketsystem.shared.infrastructure.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TicketController {

    private final PurchaseTicketsUseCase purchaseTicketsUseCase;
    private final GetPurchaseByCodeUseCase getPurchaseByCodeUseCase;
    private final ListUserPurchasesUseCase listUserPurchasesUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PurchaseResponseDTO> purchaseTickets(@Valid @RequestBody PurchaseRequestDTO request,
                                                                @RequestHeader("Authorization") String authorization) {
        Long userId = getUserIdFromToken(authorization);
        request.setUserId(userId); // Ensure user can only purchase for themselves
        PurchaseResponseDTO purchase = purchaseTicketsUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    @GetMapping("/purchase/{code}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PurchaseResponseDTO> getPurchaseByCode(@PathVariable String code) {
        PurchaseResponseDTO purchase = getPurchaseByCodeUseCase.execute(code);
        return ResponseEntity.ok(purchase);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PurchaseResponseDTO>> listUserPurchases(@PathVariable Long userId) {
        List<PurchaseResponseDTO> purchases = listUserPurchasesUseCase.execute(userId);
        return ResponseEntity.ok(purchases);
    }

    private Long getUserIdFromToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            return jwtTokenProvider.getUserIdFromToken(token);
        }
        throw new IllegalStateException("Token não encontrado");
    }
}

