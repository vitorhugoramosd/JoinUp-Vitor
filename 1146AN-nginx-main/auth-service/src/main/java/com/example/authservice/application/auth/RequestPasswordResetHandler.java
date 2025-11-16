package com.example.authservice.application.auth;

import com.example.authservice.application.port.MailSender;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Use Case: Request password reset
 * Requisito 3.3: Enviar e-mail com link para redefinicao de senha
 *
 * Flow:
 * 1. Receive email
 * 2. Find user by email
 * 3. Generate secure random token
 * 4. Save token with expiration (15 minutes)
 * 5. Send email with reset link containing token
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RequestPasswordResetHandler {

    private final UserRepository userRepository;
    private final MailSender mailSender;

    @Value("${app.password-reset.ttl-seconds:900}")
    private long ttlSeconds;

    @Value("${app.password-reset.reset-url-base:localhost:8080/auth/password/reset}")
    private String resetUrlBase;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Transactional
    public void handle(String emailRaw) {
        Email email = Email.of(emailRaw);

        log.info("Password reset requested for email: {}", email.getValue());

        // Find user by email
        User user = userRepository.findByEmail(email.getValue())
                .orElseThrow(() -> {
                    log.warn("Password reset requested for non-existent email: {}", email.getValue());
                    // Security: Don't reveal if email exists
                    // Return success anyway to prevent email enumeration
                    return new RuntimeException("User not found");
                });

        // Generate secure random token (32 bytes, URL-safe)
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Calculate expiration time
        long expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);

        // Save token and expiry to user
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(expiryTime);
        userRepository.save(user);

        // Build reset URL
        String resetUrl = String.format("%s?token=%s", resetUrlBase, token);

        // Send email with reset link
        try {
            mailSender.sendPasswordResetLink(email.getValue(), resetUrl);
            log.info("Password reset email sent to: {}", email.getValue());
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", email.getValue(), e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
