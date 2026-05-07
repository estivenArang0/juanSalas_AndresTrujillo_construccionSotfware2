package com.banco.application.usecase;

import com.banco.application.dto.request.ChangePasswordRequest;
import com.banco.application.dto.request.LoginRequest;
import com.banco.application.dto.request.RegisterRequest;
import com.banco.application.dto.response.AuthResponse;
import com.banco.application.port.input.AuthInputPort;
import com.banco.config.security.JwtService;
import com.banco.domain.model.entity.User;
import com.banco.domain.service.AuthLoginService;
import com.banco.domain.service.AuthLogoutService;
import com.banco.domain.service.AuthRegistrationService;
import com.banco.domain.service.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthInputPort {

    private final AuthLoginService loginService;
    private final AuthRegistrationService registrationService;
    private final AuthLogoutService logoutService;
    private final AuthTokenService tokenService;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = loginService.validateCredentials(request.getUsername(), request.getPassword());
        String token = jwtService.generateToken(user);
        return mapToAuthResponse(user, token);
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = registrationService.registerUser(request);
        String token = jwtService.generateToken(user);
        return mapToAuthResponse(user, token);
    }

    @Override
    public void logout(String token) {
        logoutService.logout(token);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        tokenService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
    }

    private AuthResponse mapToAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }
}
