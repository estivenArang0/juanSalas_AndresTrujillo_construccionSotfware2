package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.UpdateNaturalPersonRequest;
import com.banco.application.dto.response.NaturalPersonResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.NaturalPersonClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.NaturalPersonClientRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaturalPersonClientUpdateService {

    private final NaturalPersonClientRepository naturalPersonClientRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public NaturalPersonResponse update(String identificationNumber, UpdateNaturalPersonRequest request, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Clientes solo pueden actualizar su propia información
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT)) {
            if (!identificationNumber.equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para actualizar información de otro cliente");
            }
        } else if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE) &&
                   !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "No tienes permiso para actualizar información de clientes");
        }

        NaturalPersonClient client = naturalPersonClientRepository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con identificación: " + identificationNumber));

        // Actualizar campos permitidos
        if (request.getFullName() != null) client.setFullName(request.getFullName());
        if (request.getEmail() != null) client.setEmail(request.getEmail());
        if (request.getPhone() != null) client.setPhone(request.getPhone());
        if (request.getAddress() != null) client.setAddress(request.getAddress());

        NaturalPersonClient saved = naturalPersonClientRepository.save(client);

        auditLog.log(AuditLogRequest.builder()
                .operationType("NATURAL_PERSON_CLIENT_UPDATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(identificationNumber)
                .details(Map.of(
                        "identificationNumber", identificationNumber,
                        "updatedBy", requestingUserId
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
