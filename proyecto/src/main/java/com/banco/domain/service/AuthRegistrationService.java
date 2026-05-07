package com.banco.domain.service;

import com.banco.application.dto.request.RegisterRequest;
import com.banco.domain.model.entity.User;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {

        // Verificar que el username no esté ya registrado
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el username: " + request.getUsername());
        }

        // Verificar que el número de identificación no esté ya registrado
        if (userRepository.existsByIdentificationNumber(request.getIdentificationNumber())) {
            throw new IllegalArgumentException("Ya existe un usuario con la identificación: " + request.getIdentificationNumber());
        }

        User newUser = User.create(
                request.getRelatedEntityId(),
                request.getFullName(),
                request.getIdentificationNumber(),
                request.getEmail(),
                request.getPhone(),
                request.getBirthDate(),
                request.getAddress(),
                request.getRole(),
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        );

        return userRepository.save(newUser);
    }
}
