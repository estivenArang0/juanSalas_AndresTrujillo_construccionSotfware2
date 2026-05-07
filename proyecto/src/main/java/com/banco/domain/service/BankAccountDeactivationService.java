package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.BankAccountResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.AccountOperationNotAllowedException;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BankAccountDeactivationService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public BankAccountResponse deactivateBankAccount(String accountNumber, Long requestingUserId) {

        // Solo analistas internos o empleados de ventanilla pueden desactivar cuentas
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.TELLER_EMPLOYEE) &&
            !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados de ventanilla o analistas pueden desactivar cuentas");
        }

        // Buscar la cuenta
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada: " + accountNumber));

        // No se puede desactivar una cuenta que ya está cancelada
        if (!account.isActive()) {
            throw new AccountOperationNotAllowedException(
                    "La cuenta " + accountNumber + " no está activa. Estado actual: " + account.getStatus());
        }

        // No se puede desactivar una cuenta con saldo positivo
        if (account.getBalance() != null && account.getBalance().isPositive()) {
            throw new AccountOperationNotAllowedException(
                    "No se puede desactivar la cuenta " + accountNumber +
                    " porque tiene saldo disponible: " + account.getBalance());
        }

        // block() cambia el status a BLOCKED — es la desactivación temporal en tu entidad
        account.block();

        BankAccount saved = bankAccountRepository.save(account);

        auditLog.log(AuditLogRequest.builder()
                .operationType("BANK_ACCOUNT_DEACTIVATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(accountNumber)
                .details(Map.of(
                        "accountNumber", accountNumber,
                        "deactivatedBy", requestingUserId,
                        "newStatus", "BLOCKED"
                ))
                .build());

        return BankAccountResponse.builder()
                .id(saved.getId())
                .accountNumber(saved.getAccountNumber())
                .accountType(saved.getAccountType())
                .ownerId(saved.getOwnerId())
                .balance(saved.getBalance().getAmount())
                .currency(saved.getBalance().getCurrency())
                .status(saved.getStatus())
                .openingDate(saved.getOpeningDate())
                .build();
    }
}