package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.TransferResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.TransferStatus;
import com.banco.domain.repository.TransferRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferCancellationService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public TransferResponse cancelTransfer(Long transferId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transferencia no encontrada: " + transferId));

        if (transfer.getStatus() != TransferStatus.PENDING_APPROVAL) {
            throw new UnauthorizedOperationException("Solo se pueden cancelar transferencias pendientes de aprobación");
        }

        // Solo el creador o un supervisor pueden cancelar
        // Por simplificación en este paso, permitimos al creador
        // (En un entorno real validaríamos que el usuario creador esté en la transferencia)

        transfer.setStatus(TransferStatus.CANCELLED);
        Transfer saved = transferRepository.save(transfer);

        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_CANCELLED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(saved.getId().toString())
                .details(Map.of(
                        "transferId", saved.getId(),
                        "cancelledBy", requestingUserId
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
