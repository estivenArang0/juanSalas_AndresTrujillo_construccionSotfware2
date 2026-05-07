package com.banco.adapter.in.web.controller;

import com.banco.application.dto.request.ChangePasswordRequest;
import com.banco.application.dto.request.LoginRequest;
import com.banco.application.dto.request.RegisterRequest;
import com.banco.application.dto.response.ApiResponse;
import com.banco.application.dto.response.AuthResponse;
import com.banco.application.port.input.AuthInputPort;
import com.banco.config.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthInputPort authInputPort;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authInputPort.login(request)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("User registered successfully", authInputPort.register(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            authInputPort.logout(token.substring(7));
        }
        return ResponseEntity.ok(ApiResponse.ok("Logout successful", null));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        authInputPort.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Password changed successfully", null));
    }

    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtService.extractUserId(authHeader.substring(7));
        }
        throw new SecurityException("No token provided");
    }
}
