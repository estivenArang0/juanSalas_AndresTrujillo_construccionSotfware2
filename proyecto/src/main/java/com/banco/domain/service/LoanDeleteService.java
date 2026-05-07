package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.Loan;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.LoanStatus;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.LoanRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoanDeleteService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public void delete(Long loanId, Long requestingUserId) {

        // Solo analistas internos pueden eliminar préstamos
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden eliminar préstamos");
        }

        if (!loanExists(loanId)) {
            throw new ResourceNotFoundException(
                    "Préstamo no encontrado: " + loanId);
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Préstamo no encontrado: " + loanId));

        // No se puede eliminar un préstamo ya desembolsado
        if (loan.getStatus() == LoanStatus.DISBURSED) {
            throw new UnauthorizedOperationException(
                    "No se puede eliminar el préstamo " + loanId +
                    " porque ya fue desembolsado");
        }

        // No se puede eliminar un préstamo ya rechazado
        if (loan.getStatus() == LoanStatus.REJECTED) {
            throw new UnauthorizedOperationException(
                    "El préstamo " + loanId + " ya se encuentra rechazado");
        }

        // En un sistema bancario no se elimina físicamente — se rechaza
        // para mantener trazabilidad completa de todas las operaciones
        loan.reject(requestingUserId);
        loanRepository.save(loan);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_DELETED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(String.valueOf(loanId))
                .details(Map.of(
                        "loanId", loanId,
                        "clientId", loan.getClientId(),
                        "loanType", loan.getLoanType(),
                        "previousStatus", loan.getStatus().name(),
                        "finalStatus", LoanStatus.REJECTED.name(),
                        "deletedBy", requestingUserId
                ))
                .build());
    }

    private boolean loanExists(Long loanId) {
        return loanRepository.findById(loanId).isPresent();
    }
}