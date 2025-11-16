package com.ticketsystem.users.infrastructure.adapter.web;

import com.ticketsystem.users.application.dto.RegisterUserRequestDTO;
import com.ticketsystem.users.application.dto.UserDTO;
import com.ticketsystem.users.application.usecase.RegisterUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterUserRequestDTO request) {
        UserDTO user = registerUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}

