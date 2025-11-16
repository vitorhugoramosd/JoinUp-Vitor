package com.ticketsystem.users.application.usecase;

import com.ticketsystem.users.application.dto.ConfirmPasswordResetRequestDTO;
import com.ticketsystem.users.domain.model.User;
import com.ticketsystem.users.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmPasswordResetUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void execute(ConfirmPasswordResetRequestDTO request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new ResponseStatusException(
                        UNAUTHORIZED,
                        "Token inválido ou expirado"
                ));

        // Validate token expiration
        if (!user.isPasswordResetTokenValid()) {
            log.warn("Expired password reset token for user: {}", user.getEmail());
            user.clearResetPasswordToken();
            userRepository.save(user);
            throw new ResponseStatusException(
                    UNAUTHORIZED,
                    "Token expirado. Solicite um novo reset de senha"
            );
        }

        // Hash new password
        String hashedPassword = passwordEncoder.encode(request.getNewPassword());

        // Update user password
        user.setPassword(hashedPassword);

        // Clear reset token
        user.clearResetPasswordToken();

        userRepository.save(user);
        log.info("Password successfully reset for user: {}", user.getEmail());
    }
}

