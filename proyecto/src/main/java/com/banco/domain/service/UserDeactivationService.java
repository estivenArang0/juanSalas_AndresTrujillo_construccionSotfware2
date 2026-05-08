package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
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
public class UserDeactivationService {

    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public void deactivateUser(Long userId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden desactivar usuarios");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + userId));

        user.deactivate();
        userRepository.save(user);

        auditLog.log(AuditLogRequest.builder()
                .operationType("USER_DEACTIVATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(userId.toString())
                .details(Map.of(
                        "userId", userId,
                        "deactivatedBy", requestingUserId,
                        "status", "INACTIVE"
                ))
                .build());
    }
}
