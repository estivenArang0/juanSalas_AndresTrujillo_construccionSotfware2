package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.RegisterRequest;
import com.banco.application.dto.response.AuthResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCreateService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogOutputPort auditLog;

    public User createUser(RegisterRequest request, Long requestingUserId) {

        // Solo analistas internos pueden crear usuarios administrativos (cajeros, analistas, supervisores)
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden crear usuarios administrativos");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el username: " + request.getUsername());
        }

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

        User saved = userRepository.save(newUser);

        auditLog.log(AuditLogRequest.builder()
                .operationType("USER_CREATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(saved.getId().toString())
                .details(Map.of(
                        "username", saved.getUsername(),
                        "role", saved.getRole().name(),
                        "createdBy", requestingUserId
                ))
                .build());

        return saved;
    }
}
