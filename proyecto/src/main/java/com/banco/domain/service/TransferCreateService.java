package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.CreateTransferRequest;
import com.banco.application.dto.response.TransferResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.Money;
import com.banco.domain.model.valueobject.TransferStatus;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.TransferRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferCreateService {

    private final TransferRepository transferRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final TransferDomainService transferDomainService;
    private final AuditLogOutputPort auditLog;

    public TransferResponse createTransfer(CreateTransferRequest request, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Validar cuenta de origen
        BankAccount sourceAccount = bankAccountRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta de origen no encontrada: " + request.getSourceAccountNumber()));

        // Validar cuenta de destino
        BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta de destino no encontrada: " + request.getDestinationAccountNumber()));

        // Permisos: Clientes solo pueden transferir desde sus propias cuentas
        if (!sourceAccount.getOwnerId().equals(requestingUser.getIdentificationNumber())) {
            throw new UnauthorizedOperationException("No tienes permiso para transferir desde esta cuenta");
        }

        sourceAccount.validateIsOperational();
        destinationAccount.validateIsOperational();

        Money amount = Money.of(request.getAmount(), sourceAccount.getBalance().getCurrency());
        
        // Verificar saldo suficiente
        if (!sourceAccount.hasSufficientBalance(amount)) {
            throw new UnauthorizedOperationException("Saldo insuficiente en la cuenta de origen");
        }

        // Determinar estado inicial (requiere aprobación si es monto alto)
        TransferStatus initialStatus = transferDomainService.determineInitialStatus(amount);

        Transfer transfer = Transfer.builder()
                .sourceAccountNumber(request.getSourceAccountNumber())
                .destinationAccountNumber(request.getDestinationAccountNumber())
                .amount(amount)
                .description(request.getDescription())
                .status(initialStatus)
                .requestedAt(LocalDateTime.now())
                .build();

        Transfer saved = transferRepository.save(transfer);

        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_CREATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(saved.getId().toString())
                .details(Map.of(
                        "transferId", saved.getId(),
                        "source", request.getSourceAccountNumber(),
                        "destination", request.getDestinationAccountNumber(),
                        "amount", request.getAmount(),
                        "status", initialStatus.name()
                ))
                .build());

        return toResponse(saved);
    }

    private TransferResponse toResponse(Transfer transfer) {
        return TransferResponse.builder()
                .id(transfer.getId())
                .sourceAccountNumber(transfer.getSourceAccountNumber())
                .destinationAccountNumber(transfer.getDestinationAccountNumber())
                .amount(transfer.getAmount().getAmount())
                .currency(transfer.getAmount().getCurrency())
                .description(transfer.getDescription())
                .status(transfer.getStatus())
                .requestedAt(transfer.getRequestedAt())
                .executedAt(transfer.getExecutedAt())
                .build();
    }
}
