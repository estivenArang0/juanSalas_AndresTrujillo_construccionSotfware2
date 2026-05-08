package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.TransferResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.TransferStatus;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.TransferRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferExecutionService {

    private final TransferRepository transferRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    @Transactional
    public TransferResponse executeTransfer(Long transferId, Long executingUserId) {

        User executingUser = userRepository.findById(executingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + executingUserId));

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transferencia no encontrada: " + transferId));

        if (transfer.getStatus() == TransferStatus.EXECUTED) {
            throw new UnauthorizedOperationException("La transferencia ya ha sido ejecutada");
        }

        if (transfer.getStatus() == TransferStatus.CANCELLED) {
            throw new UnauthorizedOperationException("No se puede ejecutar una transferencia cancelada");
        }

        // Si requiere aprobación y el usuario es el mismo que la creó, no puede aprobarla él mismo (si es corporativo)
        // Por ahora, validamos roles básicos
        if (transfer.getStatus() == TransferStatus.PENDING_APPROVAL) {
            if (!executingUser.hasRole(UserRole.COMPANY_SUPERVISOR) && 
                !executingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
                throw new UnauthorizedOperationException("No tienes permisos para aprobar esta transferencia");
            }
        }

        BankAccount source = bankAccountRepository.findByAccountNumber(transfer.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta de origen no encontrada"));
        
        BankAccount destination = bankAccountRepository.findByAccountNumber(transfer.getDestinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta de destino no encontrada"));

        // Validar fondos nuevamente por si cambiaron desde la creación
        if (!source.hasSufficientBalance(transfer.getAmount())) {
            transfer.setStatus(TransferStatus.REJECTED);
            transferRepository.save(transfer);
            throw new UnauthorizedOperationException("Saldo insuficiente al momento de la ejecución");
        }

        // Ejecutar transacción
        source.debit(transfer.getAmount());
        destination.credit(transfer.getAmount());

        transfer.setStatus(TransferStatus.EXECUTED);
        transfer.setExecutedAt(LocalDateTime.now());

        bankAccountRepository.save(source);
        bankAccountRepository.save(destination);
        Transfer saved = transferRepository.save(transfer);

        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_EXECUTED")
                .operationDateTime(LocalDateTime.now())
                .userId(executingUserId)
                .userRole(executingUser.getRole().name())
                .affectedProductId(saved.getId().toString())
                .details(Map.of(
                        "transferId", saved.getId(),
                        "amount", transfer.getAmount().getAmount(),
                        "executedBy", executingUserId
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
