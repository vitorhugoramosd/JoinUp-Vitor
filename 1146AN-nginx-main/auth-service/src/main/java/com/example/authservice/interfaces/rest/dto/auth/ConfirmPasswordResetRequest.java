package com.example.authservice.interfaces.rest.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for confirming password reset with token and new password
 * Requisito 3.3: Recuperacao de senha
 */
public record ConfirmPasswordResetRequest(
        @NotBlank(message = "Reset token is required")
        String token,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String newPassword
) {
}
