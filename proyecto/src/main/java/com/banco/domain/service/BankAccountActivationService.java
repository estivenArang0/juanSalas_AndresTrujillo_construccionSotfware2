package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.BankAccountResponse;
import com.banco.application.port.output.AuditLogOutputPort;
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
public class BankAccountActivationService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public BankAccountResponse activateBankAccount(String accountNumber, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.TELLER_EMPLOYEE) &&
            !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados de ventanilla o analistas pueden activar cuentas");
        }

        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada: " + accountNumber));

        account.activate();

        BankAccount saved = bankAccountRepository.save(account);

        auditLog.log(AuditLogRequest.builder()
                .operationType("BANK_ACCOUNT_ACTIVATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(accountNumber)
                .details(Map.of(
                        "accountNumber", accountNumber,
                        "activatedBy", requestingUserId,
                        "newStatus", "ACTIVE"
                ))
                .build());

        return BankAccountResponse.builder()
                .id(saved.getId())
                .accountNumber(saved.getAccountNumber())
                .accountType(saved.getAccountType())
                .ownerId(saved.getOwnerId())
                .balance(saved.getBalance() != null ? saved.getBalance().getAmount() : null)
                .currency(saved.getBalance() != null ? saved.getBalance().getCurrency() : null)
                .status(saved.getStatus())
                .openingDate(saved.getOpeningDate())
                .build();
    }
}