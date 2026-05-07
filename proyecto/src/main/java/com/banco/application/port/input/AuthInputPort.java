package com.banco.application.port.input;

import com.banco.application.dto.request.ChangePasswordRequest;
import com.banco.application.dto.request.LoginRequest;
import com.banco.application.dto.request.RegisterRequest;
import com.banco.application.dto.response.AuthResponse;

public interface AuthInputPort {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    void logout(String token);
    void changePassword(Long userId, ChangePasswordRequest request);
}
