package com.banco.shared;

import com.banco.adapter.in.web.security.UserPrincipal;
import com.banco.domain.exception.EntityNotFoundException;
import com.banco.domain.model.entity.User;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserHelper {
    private final UserRepository usuarioRepository;

    public User getUsuarioActual() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findById(principal.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User autenticado no encontrado"));
    }

    public Long getIdUsuarioActual() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return principal.getUserId();
    }
}
