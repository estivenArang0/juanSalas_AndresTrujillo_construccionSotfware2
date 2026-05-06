package com.bank.app.adapter.in.web.controller;

import com.bank.app.application.dto.request.LoginRequest;
import com.bank.app.application.dto.response.ApiResponse;
import com.bank.app.application.dto.response.AuthResponse;
import com.bank.app.application.port.input.AuthInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthInputPort authInputPort;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authInputPort.login(request)));
    }
}
