package com.banco.domain.service;

import com.banco.application.dto.request.ApproveLoanRequest;
import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.LoanResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.Loan;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.LoanStatus;
import com.banco.domain.model.valueobject.Money;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.LoanRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoanApprovalService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public LoanResponse approveLoan(Long loanId, ApproveLoanRequest request, Long analystId) {

        // Solo analistas internos pueden aprobar préstamos
        User analyst = userRepository.findById(analystId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + analystId));

        if (!analyst.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden aprobar préstamos");
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Préstamo no encontrado: " + loanId));

        // approve() en la entidad valida que el estado sea UNDER_REVIEW
        // y lanza InvalidLoanStateTransitionException si no lo es
        loan.approve(
                analystId,
                Money.of(request.getApprovedAmount(), request.getCurrency()),
                request.getInterestRate()
        );

        Loan saved = loanRepository.save(loan);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_APPROVED")
                .operationDateTime(LocalDateTime.now())
                .userId(analystId)
                .userRole(UserRole.INTERNAL_ANALYST.name())
                .affectedProductId(String.valueOf(loanId))
                .details(Map.of(
                        "loanId", loanId,
                        "clientId", loan.getClientId(),
                        "approvedAmount", request.getApprovedAmount(),
                        "interestRate", request.getInterestRate(),
                        "previousStatus", LoanStatus.UNDER_REVIEW.name(),
                        "newStatus", LoanStatus.APPROVED.name(),
                        "approvedBy", analystId
                ))
                .build());

        return toResponse(saved);
    }

    public LoanResponse rejectLoan(Long loanId, Long analystId) {

        // Solo analistas internos pueden rechazar préstamos
        User analyst = userRepository.findById(analystId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + analystId));

        if (!analyst.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden rechazar préstamos");
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Préstamo no encontrado: " + loanId));

        // reject() en la entidad valida que el estado sea UNDER_REVIEW
        // y registra la fecha de rechazo para trazabilidad
        loan.reject(analystId);

        Loan saved = loanRepository.save(loan);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_REJECTED")
                .operationDateTime(LocalDateTime.now())
                .userId(analystId)
                .userRole(UserRole.INTERNAL_ANALYST.name())
                .affectedProductId(String.valueOf(loanId))
                .details(Map.of(
                        "loanId", loanId,
                        "clientId", loan.getClientId(),
                        "previousStatus", LoanStatus.UNDER_REVIEW.name(),
                        "newStatus", LoanStatus.REJECTED.name(),
                        "rejectedBy", analystId,
                        "rejectionDate", saved.getRejectionDate().toString()
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