package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.TransferResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.TransferStatus;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.TransferRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferUpdateService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    private static final int EXPIRATION_HOURS = 24;

    public TransferResponse approveTransfer(Long transferId, Long supervisorId) {

        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supervisor no encontrado: " + supervisorId));

        // Solo supervisores corporativos o analistas pueden aprobar montos altos
        if (!supervisor.hasRole(UserRole.COMPANY_SUPERVISOR) && 
            !supervisor.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException("No tienes permisos para aprobar transferencias");
        }

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transferencia no encontrada: " + transferId));

        if (transfer.getStatus() != TransferStatus.PENDING_APPROVAL) {
            throw new UnauthorizedOperationException("La transferencia no está pendiente de aprobación");
        }

        // Verificar si ha expirado
        if (isExpired(transfer)) {
            expireTransfer(transfer);
            throw new UnauthorizedOperationException("La transferencia ha expirado y no puede ser aprobada");
        }

        transfer.setStatus(TransferStatus.APPROVED);
        Transfer saved = transferRepository.save(transfer);

        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_APPROVED")
                .operationDateTime(LocalDateTime.now())
                .userId(supervisorId)
                .userRole(supervisor.getRole().name())
                .affectedProductId(transferId.toString())
                .details(Map.of(
                        "transferId", transferId,
                        "approvedBy", supervisorId
                ))
                .build());

        return toResponse(saved);
    }

    public void processExpirations() {
        List<Transfer> pendingTransfers = transferRepository.findAllByStatus(TransferStatus.PENDING_APPROVAL);
        
        for (Transfer transfer : pendingTransfers) {
            if (isExpired(transfer)) {
                expireTransfer(transfer);
            }
        }
    }

    private boolean isExpired(Transfer transfer) {
        return transfer.getRequestedAt().plusHours(EXPIRATION_HOURS).isBefore(LocalDateTime.now());
    }

    private void expireTransfer(Transfer transfer) {
        transfer.setStatus(TransferStatus.EXPIRED);
        transferRepository.save(transfer);
        
        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_EXPIRED")
                .operationDateTime(LocalDateTime.now())
                .userId(0L) // Sistema
                .userRole("SYSTEM")
                .affectedProductId(transfer.getId().toString())
                .details(Map.of(
                        "transferId", transfer.getId(),
                        "reason", "Approval timeout"
                ))
                .build());
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
