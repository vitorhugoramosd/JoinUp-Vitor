package com.ticketsystem.users.application.usecase;

import com.ticketsystem.shared.infrastructure.security.JwtTokenProvider;
import com.ticketsystem.users.application.dto.LoginRequestDTO;
import com.ticketsystem.users.application.dto.LoginResponseDTO;
import com.ticketsystem.users.application.dto.UserDTO;
import com.ticketsystem.users.domain.model.User;
import com.ticketsystem.users.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponseDTO execute(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Credenciais inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Credenciais inválidas");
        }

        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        return LoginResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationInMs() / 1000)
                .user(UserDTO.fromDomain(user))
                .build();
    }
}

