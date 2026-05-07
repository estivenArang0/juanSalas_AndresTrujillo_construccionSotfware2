package com.banco.adapter.in.web.security;

import com.banco.domain.model.entity.User;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado: " + username));
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), 
                u.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
        );
    }
}
