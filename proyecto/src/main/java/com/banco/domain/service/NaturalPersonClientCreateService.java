package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.CreateNaturalPersonRequest;
import com.banco.application.dto.response.NaturalPersonResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.DuplicateIdentificationException;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.NaturalPersonClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.model.valueobject.UserStatus;
import com.banco.domain.repository.NaturalPersonClientRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaturalPersonClientCreateService {

    private final NaturalPersonClientRepository naturalPersonClientRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public NaturalPersonResponse create(CreateNaturalPersonRequest request, Long requestingUserId) {

        // Solo empleados comerciales o analistas pueden registrar nuevos clientes
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE) &&
            !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados comerciales o analistas pueden registrar nuevos clientes");
        }

        // Verificar que el cliente no esté ya registrado por identificación
        if (naturalPersonClientRepository.existsByIdentificationNumber(request.getIdentificationNumber())) {
            throw new DuplicateIdentificationException(
                    "Ya existe un cliente registrado con la identificación: " + request.getIdentificationNumber());
        }

        NaturalPersonClient newClient = NaturalPersonClient.builder()
                .fullName(request.getFullName())
                .identificationNumber(request.getIdentificationNumber())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .address(request.getAddress())
                .status(UserStatus.ACTIVE)
                .build();

        newClient.validateRequiredFields();
        NaturalPersonClient saved = naturalPersonClientRepository.save(newClient);

        auditLog.log(AuditLogRequest.builder()
                .operationType("NATURAL_PERSON_CLIENT_CREATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(saved.getIdentificationNumber())
                .details(Map.of(
                        "fullName", saved.getFullName(),
                        "identificationNumber", saved.getIdentificationNumber(),
                        "createdBy", requestingUserId
                ))
                .build());

        return NaturalPersonResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .identificationNumber(saved.getIdentificationNumber())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .birthDate(saved.getBirthDate())
                .address(saved.getAddress())
                .build();
    }
}
