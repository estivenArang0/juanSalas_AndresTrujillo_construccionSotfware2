package com.banco.domain.service;

import com.banco.config.security.JwtService;
import com.banco.application.dto.response.AuthResponse;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.User;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse refreshToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("El token no puede ser nulo o vacío");
        }

        if (!jwtService.validateToken(token)) {
            throw new UnauthorizedOperationException("Token inválido o expirado");
        }

        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + username));

        user.validateIsActive();

        // Generar nuevo token con los datos actuales del usuario
        String newToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(newToken)
                .username(user.getUsername())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("La nueva contraseña debe tener mínimo 8 caracteres");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + userId));

        user.validateIsActive();

        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new UnauthorizedOperationException("La contraseña actual es incorrecta");
        }

        // Verificar que la nueva contraseña sea diferente a la actual
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
