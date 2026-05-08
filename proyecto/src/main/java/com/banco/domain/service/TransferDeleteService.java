package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferDeleteService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public void deleteTransfer(Long transferId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden eliminar registros de transferencias");
        }

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transferencia no encontrada: " + transferId));

        // No se puede eliminar una transferencia ya ejecutada (por auditoría)
        if (transfer.getStatus() == TransferStatus.EXECUTED) {
            throw new UnauthorizedOperationException("No se puede eliminar una transferencia ya ejecutada");
        }

        // Realizar borrado físico o lógico según política. Usaremos anulación formal.
        transfer.setStatus(TransferStatus.CANCELLED);
        transferRepository.save(transfer);

        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_DELETED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(transferId.toString())
                .details(Map.of(
                        "transferId", transferId,
                        "deletedBy", requestingUserId,
                        "status", "CANCELLED"
                ))
                .build());
        
        // transferRepository.deleteById(transferId);
    }
}
