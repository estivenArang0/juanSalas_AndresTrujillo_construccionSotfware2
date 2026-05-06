package com.bank.app.application.port.input;
import com.bank.app.application.dto.request.LoginRequest;
import com.bank.app.application.dto.response.AuthResponse;
public interface AuthInputPort {
    AuthResponse login(LoginRequest request);
}
