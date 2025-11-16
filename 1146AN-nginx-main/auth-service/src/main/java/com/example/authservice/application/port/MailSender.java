package com.example.authservice.application.port;

import java.time.Instant;

/**
 * Port (Interface) for sending emails
 * Implementation will be in infrastructure layer
 */
public interface MailSender {
    /**
     * Send magic link email for passwordless login
     */
    void sendMagicLink(
        String toEmail,
        String magicUrl,
        Instant expiresAt
    );

    /**
     * Send password reset link email
     * Requisito 3.3: Enviar e-mail com link para redefinicao de senha
     */
    void sendPasswordResetLink(
        String toEmail,
        String resetUrl
    );
}
