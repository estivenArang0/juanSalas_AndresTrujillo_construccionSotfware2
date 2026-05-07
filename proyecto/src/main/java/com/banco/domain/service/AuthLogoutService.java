package com.banco.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthLogoutService {

    // Set thread-safe para almacenar tokens invalidados
    private final Set<String> tokenBlacklist = 
            Collections.synchronizedSet(new HashSet<>());

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token no puede ser nulo o vacío");
        }
        tokenBlacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
