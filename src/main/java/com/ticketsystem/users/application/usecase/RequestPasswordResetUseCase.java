package com.ticketsystem.users.application.usecase;

import com.ticketsystem.shared.infrastructure.email.EmailService;
import com.ticketsystem.users.domain.model.User;
import com.ticketsystem.users.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestPasswordResetUseCase {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.password-reset.ttl-seconds:900}")
    private long ttlSeconds;

    @Value("${app.password-reset.reset-url-base:http://localhost:8080/auth/password/reset}")
    private String resetUrlBase;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Transactional
    public void execute(String email) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.warn("Password reset requested for non-existent email: {}", email);
                    // Security: Don't reveal if email exists
                    return null;
                });

        if (user != null) {
            // Generate secure random token (32 bytes, URL-safe)
            byte[] randomBytes = new byte[32];
            SECURE_RANDOM.nextBytes(randomBytes);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

            // Calculate expiration time
            LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(ttlSeconds);

            // Save token and expiry to user
            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(expiryTime);
            userRepository.save(user);

            // Build reset URL
            String resetUrl = String.format("%s?token=%s", resetUrlBase, token);

            // Send email with reset link
            emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
            log.info("Password reset email sent to: {}", user.getEmail());
        } else {
            log.info("Password reset request for unknown email (silent success): {}", email);
        }
    }
}

