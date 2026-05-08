package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.UpdateUserRequest;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public User updateUser(Long userId, UpdateUserRequest request, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario a actualizar no encontrado: " + userId));

        // Un usuario puede actualizar sus propios datos (limitados)
        // Un analista puede actualizar a cualquiera
        if (!requestingUserId.equals(userId) && !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException("No tienes permiso para actualizar este usuario");
        }

        if (request.getFullName() != null) userToUpdate.setFullName(request.getFullName());
        if (request.getEmail() != null) userToUpdate.setEmail(request.getEmail());
        if (request.getPhone() != null) userToUpdate.setPhone(request.getPhone());
        if (request.getAddress() != null) userToUpdate.setAddress(request.getAddress());
        
        // Solo analistas pueden cambiar roles
        if (request.getRole() != null && requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            userToUpdate.setRole(request.getRole());
        }

        User saved = userRepository.save(userToUpdate);

        auditLog.log(AuditLogRequest.builder()
                .operationType("USER_UPDATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(saved.getId().toString())
                .details(Map.of(
                        "userId", saved.getId(),
                        "updatedBy", requestingUserId
                ))
                .build());

        return saved;
    }
}
