package com.banco.domain.service;

import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.model.entity.User;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User validateCredentials(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResourceNotFoundException("Usuario o contraseña incorrectos");
        }

        user.validateIsActive();
        
        return user;
    }
}
