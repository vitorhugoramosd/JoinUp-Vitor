package com.example.authservice.application.auth;

import com.example.authservice.application.port.PasswordHasher;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Use Case: Confirm password reset with token
 * Requisito 3.3: Redefinicao de senha com token
 *
 * Flow:
 * 1. Receive token and new password
 * 2. Find user by token
 * 3. Validate token expiration
 * 4. Hash new password
 * 5. Update user password
 * 6. Clear reset token
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmPasswordResetHandler {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public void handle(String token, String newPassword) {
        log.info("Password reset confirmation with token: {}", token.substring(0, 8) + "...");

        // Find user by reset token
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> {
                    log.warn("Invalid password reset token");
                    return new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Token invalido ou expirado"
                    );
                });

        // Validate token expiration
        if (!user.isResetPasswordTokenValid()) {
            log.warn("Expired password reset token for user: {}", user.getEmail().getValue());
            user.clearResetPasswordToken();
            userRepository.save(user);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token expirado. Solicite um novo reset de senha"
            );
        }

        // Hash new password
        String hashedPassword = passwordHasher.hash(newPassword);

        // Update user password
        user.setPassword(hashedPassword);

        // Clear reset token
        user.clearResetPasswordToken();

        userRepository.save(user);

        log.info("Password successfully reset for user: {}", user.getEmail().getValue());
    }
}
