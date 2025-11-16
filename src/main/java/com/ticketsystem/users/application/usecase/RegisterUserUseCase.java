package com.ticketsystem.users.application.usecase;

import com.ticketsystem.users.application.dto.RegisterUserRequestDTO;
import com.ticketsystem.users.application.dto.UserDTO;
import com.ticketsystem.users.domain.model.User;
import com.ticketsystem.users.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO execute(RegisterUserRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    CONFLICT,
                    "Email já cadastrado. Se você já possui uma conta, faça login."
            );
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User.Role role = request.getRole() != null ? request.getRole() : User.Role.USER;

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(hashedPassword)
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        return UserDTO.fromDomain(savedUser);
    }
}

