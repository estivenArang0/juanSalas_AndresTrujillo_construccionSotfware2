package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.BankAccountResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.AccountOperationNotAllowedException;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.AccountStatus;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BankAccountUpdateService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    // En un banco lo único actualizable de una cuenta es su estado
    public BankAccountResponse updateStatus(String accountNumber, AccountStatus newStatus, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Solo analistas o empleados de ventanilla pueden cambiar estado de cuentas
        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST) &&
            !requestingUser.hasRole(UserRole.TELLER_EMPLOYEE)) {
            throw new UnauthorizedOperationException(
                    "No tienes permiso para modificar el estado de una cuenta");
        }

        if (!accountExists(accountNumber)) {
            throw new ResourceNotFoundException(
                    "Cuenta no encontrada: " + accountNumber);
        }

        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada: " + accountNumber));

        AccountStatus previousStatus = account.getStatus();

        // Aplicar el cambio de estado usando los métodos de la entidad
        switch (newStatus) {
            case ACTIVE   -> account.activate();
            case BLOCKED  -> account.block();
            case CANCELLED -> {
                if (account.getBalance() != null && account.getBalance().isPositive()) {
                    throw new AccountOperationNotAllowedException(
                            "No se puede cancelar la cuenta " + accountNumber +
                            " porque tiene saldo disponible: " + account.getBalance());
                }
                account.cancel();
            }
            default -> throw new AccountOperationNotAllowedException(
                    "Estado no válido: " + newStatus);
        }

        BankAccount saved = bankAccountRepository.save(account);

        auditLog.log(AuditLogRequest.builder()
                .operationType("BANK_ACCOUNT_STATUS_UPDATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(accountNumber)
                .details(Map.of(
                        "accountNumber", accountNumber,
                        "previousStatus", previousStatus.name(),
                        "newStatus", newStatus.name(),
                        "updatedBy", requestingUserId
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

    private boolean accountExists(String accountNumber) {
        return bankAccountRepository.existsByAccountNumber(accountNumber);
    }
}