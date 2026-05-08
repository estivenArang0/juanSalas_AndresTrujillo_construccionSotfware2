package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.DisburseLoanRequest;
import com.banco.application.dto.response.LoanResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.AccountOperationNotAllowedException;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.Loan;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.LoanRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoanDisbursementService {

    private final LoanRepository loanRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    @Transactional
    public LoanResponse disburseLoan(Long loanId, DisburseLoanRequest request, Long analystId) {

        // Solo analistas internos pueden desembolsar préstamos
        User analyst = userRepository.findById(analystId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + analystId));

        if (!analyst.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden desembolsar préstamos");
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Préstamo no encontrado: " + loanId));

        // Verificar la cuenta de destino
        BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta de destino no encontrada: " + request.getDestinationAccountNumber()));

        // Verificar que la cuenta pertenezca al cliente del préstamo
        if (!destinationAccount.getOwnerId().equals(loan.getClientId())) {
            throw new UnauthorizedOperationException(
                    "La cuenta de destino no pertenece al titular del préstamo");
        }

        // Verificar que la cuenta esté activa
        destinationAccount.validateIsOperational();

        // Aplicar desembolso en la entidad Loan
        loan.disburse(request.getDestinationAccountNumber());

        // Aumentar el saldo en la cuenta de destino
        destinationAccount.credit(loan.getApprovedAmount());

        // Persistir cambios
        bankAccountRepository.save(destinationAccount);
        Loan saved = loanRepository.save(loan);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_DISBURSED")
                .operationDateTime(LocalDateTime.now())
                .userId(analystId)
                .userRole(UserRole.INTERNAL_ANALYST.name())
                .affectedProductId(String.valueOf(loanId))
                .details(Map.of(
                        "loanId", loanId,
                        "clientId", loan.getClientId(),
                        "amount", loan.getApprovedAmount().getAmount(),
                        "destinationAccount", request.getDestinationAccountNumber(),
                        "disbursedBy", analystId
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
