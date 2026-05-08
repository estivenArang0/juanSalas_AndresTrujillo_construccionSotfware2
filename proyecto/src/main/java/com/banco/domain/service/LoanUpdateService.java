package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.LoanResponse;
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
public class LoanUpdateService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public LoanResponse updateLoanStatus(Long loanId, LoanStatus newStatus, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden actualizar el estado de un préstamo manualmente");
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Préstamo no encontrado: " + loanId));

        LoanStatus previousStatus = loan.getStatus();
        loan.setStatus(newStatus);

        Loan saved = loanRepository.save(loan);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_STATUS_UPDATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(UserRole.INTERNAL_ANALYST.name())
                .affectedProductId(String.valueOf(loanId))
                .details(Map.of(
                        "loanId", loanId,
                        "previousStatus", previousStatus.name(),
                        "newStatus", newStatus.name(),
                        "updatedBy", requestingUserId
                ))
                .build());

        return toResponse(saved);
    }

    private LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .loanType(loan.getLoanType())
                .clientId(loan.getClientId())
                .requestedAmount(loan.getRequestedAmount().getAmount())
                .approvedAmount(loan.getApprovedAmount() != null
                        ? loan.getApprovedAmount().getAmount() : null)
                .currency(loan.getRequestedAmount().getCurrency())
                .interestRate(loan.getInterestRate())
                .termMonths(loan.getTermMonths())
                .status(loan.getStatus())
                .approvalDate(loan.getApprovalDate())
                .disbursementDate(loan.getDisbursementDate())
                .disbursementAccountNumber(loan.getDisbursementAccountNumber())
                .build();
    }
}
