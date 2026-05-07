package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.NaturalPersonClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.NaturalPersonClientRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaturalPersonClientDeleteService {

    private final NaturalPersonClientRepository naturalPersonClientRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public void delete(String identificationNumber, Long requestingUserId) {

        // Solo analistas internos pueden eliminar clientes
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden eliminar clientes");
        }

        NaturalPersonClient client = naturalPersonClientRepository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con identificación: " + identificationNumber));

        // No se puede eliminar un cliente que tiene cuentas bancarias activas
        boolean hasActiveAccounts = bankAccountRepository.findByOwnerId(identificationNumber)
                .stream()
                .anyMatch(account -> account.isActive());

        if (hasActiveAccounts) {
            throw new UnauthorizedOperationException(
                    "No se puede eliminar el cliente porque tiene cuentas bancarias activas");
        }

        // Borrado lógico: desactivar antes de eliminar físicamente (o solo desactivar según política)
        client.deactivate();
        naturalPersonClientRepository.save(client);

        auditLog.log(AuditLogRequest.builder()
                .operationType("NATURAL_PERSON_CLIENT_DELETED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(identificationNumber)
                .details(Map.of(
                        "identificationNumber", identificationNumber,
                        "deletedBy", requestingUserId
                ))
                .build());
        
        // naturalPersonClientRepository.deleteByIdentificationNumber(identificationNumber);
    }
}
