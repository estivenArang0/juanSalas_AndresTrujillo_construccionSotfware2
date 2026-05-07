package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.CreateBankAccountRequest;
import com.banco.application.dto.response.BankAccountResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.AccountOperationNotAllowedException;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.AccountStatus;
import com.banco.domain.model.valueobject.Money;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountCreateService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public BankAccountResponse create(CreateBankAccountRequest request, Long requestingUserId) {

        // Solo empleados de ventanilla pueden abrir cuentas
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.TELLER_EMPLOYEE)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados de ventanilla pueden abrir nuevas cuentas");
        }

        // Verificar que el cliente dueño de la cuenta existe y está activo
        User owner = userRepository.findByIdentificationNumber(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con identificación: " + request.getOwnerId()));

        owner.validateIsActive();

        // Verificar que el cliente no tenga ya una cuenta del mismo tipo
        if (accountExistsForClient(request.getOwnerId(), request)) {
            throw new AccountOperationNotAllowedException(
                    "El cliente ya tiene una cuenta de tipo " + request.getAccountType());
        }

        // Generar número de cuenta único
        String accountNumber = generateAccountNumber(request.getAccountType().name());

        BankAccount newAccount = BankAccount.builder()
                .accountNumber(accountNumber)
                .accountType(request.getAccountType())
                .ownerId(request.getOwnerId())
                .balance(Money.zero(request.getCurrency()))
                .status(AccountStatus.ACTIVE)
                .openingDate(LocalDate.now())
                .build();

        BankAccount saved = bankAccountRepository.save(newAccount);

        auditLog.log(AuditLogRequest.builder()
                .operationType("BANK_ACCOUNT_CREATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(accountNumber)
                .details(Map.of(
                        "accountNumber", accountNumber,
                        "accountType", request.getAccountType().name(),
                        "ownerId", request.getOwnerId(),
                        "currency", request.getCurrency(),
                        "openedBy", requestingUserId
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

    private boolean accountExistsForClient(String ownerId, CreateBankAccountRequest request) {
        return bankAccountRepository.findByOwnerId(ownerId)
                .stream()
                .anyMatch(acc -> acc.getAccountType() == request.getAccountType());
    }

    private String generateAccountNumber(String accountType) {
        String prefix = accountType.startsWith("SAVINGS") ? "1001" : "2001";
        String unique = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return prefix + "-" + unique;
    }
}
