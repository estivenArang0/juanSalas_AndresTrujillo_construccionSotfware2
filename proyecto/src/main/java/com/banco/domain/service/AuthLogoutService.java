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

    public boolean isTokenBlacklisted(String token) {scm-history-item:c%3A%5CUsers%5CUsuario%5CDesktop%5Cproyecto-3%5CjuanSalas_AndresTrujillo_construccionSotfware2?%7B%22repositoryId%22%3A%22scm0%22%2C%22historyItemId%22%3A%2288bd0a4d4632b854899a6370f134b6b9d2167b46%22%2C%22historyItemParentId%22%3A%22f6a24dd852c04f76ef5b8303491a4492e99f0fb6%22%2C%22historyItemDisplayId%22%3A%2288bd0a4%22%7D
        return tokenBlacklist.contains(token);
    }
}
