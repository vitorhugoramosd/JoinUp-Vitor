package com.ticketsystem.users.infrastructure.adapter.web;

import com.ticketsystem.users.application.dto.ConfirmPasswordResetRequestDTO;
import com.ticketsystem.users.application.dto.LoginRequestDTO;
import com.ticketsystem.users.application.dto.LoginResponseDTO;
import com.ticketsystem.users.application.usecase.ConfirmPasswordResetUseCase;
import com.ticketsystem.users.application.usecase.LoginUseCase;
import com.ticketsystem.users.application.usecase.RequestPasswordResetUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RequestPasswordResetUseCase requestPasswordResetUseCase;
    private final ConfirmPasswordResetUseCase confirmPasswordResetUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = loginUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password/reset/request")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        requestPasswordResetUseCase.execute(email);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<Void> confirmPasswordReset(@Valid @RequestBody ConfirmPasswordResetRequestDTO request) {
        confirmPasswordResetUseCase.execute(request);
        return ResponseEntity.ok().build();
    }
}

