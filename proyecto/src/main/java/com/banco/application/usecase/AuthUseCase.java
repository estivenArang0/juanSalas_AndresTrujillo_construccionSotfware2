package com.bank.app.application.usecase;

import com.bank.app.application.dto.request.LoginRequest;
import com.bank.app.application.dto.response.AuthResponse;
import com.bank.app.application.port.input.AuthInputPort;
import com.bank.app.config.security.JwtService;
import com.bank.app.domain.model.entity.User;
import com.bank.app.domain.repository.UserRepository;
import com.bank.app.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthInputPort {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
            throw new ResourceNotFoundException("Invalid username or password");

        user.validateIsActive();

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }
}
