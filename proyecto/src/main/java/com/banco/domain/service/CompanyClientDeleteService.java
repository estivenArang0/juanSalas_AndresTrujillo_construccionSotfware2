package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.DuplicateIdentificationException;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.CompanyClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.CorporateCustomerRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyClientDeleteService {

    private final CorporateCustomerRepository corporateCustomerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public void delete(String taxId, Long requestingUserId) {

        // Solo analistas internos pueden eliminar clientes empresa
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden eliminar clientes empresa");
        }

        // Verificar que la empresa existe
        if (!companyClientExists(taxId)) {
            throw new ResourceNotFoundException(
                    "No existe una empresa registrada con el NIT: " + taxId);
        }

        CompanyClient company = corporateCustomerRepository.findByTaxId(taxId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una empresa registrada con el NIT: " + taxId));

        // No se puede eliminar una empresa que tiene cuentas bancarias activas
        boolean hasActiveAccounts = bankAccountRepository.findByOwnerId(taxId)
                .stream()
                .anyMatch(account -> account.isActive());

        if (hasActiveAccounts) {
            throw new UnauthorizedOperationException(
                    "No se puede eliminar la empresa con NIT " + taxId +
                    " porque tiene cuentas bancarias activas");
        }

        // Desactivar antes de eliminar para dejar trazabilidad
        company.deactivate();
        corporateCustomerRepository.save(company);

        auditLog.log(AuditLogRequest.builder()
                .operationType("COMPANY_CLIENT_DELETED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(taxId)
                .details(Map.of(
                        "taxId", taxId,
                        "legalName", company.getBusinessName(),
                        "deletedBy", requestingUserId,
                        "finalStatus", "INACTIVE"
                ))
                .build());
    }

    private boolean companyClientExists(String taxId) {
        return corporateCustomerRepository.existsByTaxId(taxId);
    }
}