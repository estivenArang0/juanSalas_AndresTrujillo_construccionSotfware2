package com.bank.app.application.usecase;

import com.bank.app.application.dto.request.ApproveLoanRequest;
import com.bank.app.application.dto.request.AuditLogRequest;
import com.bank.app.application.dto.request.CreateLoanRequest;
import com.bank.app.application.dto.request.DisburseLoanRequest;
import com.bank.app.application.dto.response.LoanResponse;
import com.bank.app.application.port.input.LoanInputPort;
import com.bank.app.application.port.output.AuditLogOutputPort;
import com.bank.app.domain.exception.ResourceNotFoundException;
import com.bank.app.domain.exception.UnauthorizedOperationException;
import com.bank.app.domain.model.entity.BankAccount;
import com.bank.app.domain.model.entity.Loan;
import com.bank.app.domain.model.entity.User;
import com.bank.app.domain.model.valueobject.LoanStatus;
import com.bank.app.domain.model.valueobject.Money;
import com.bank.app.domain.model.valueobject.UserRole;
import com.bank.app.domain.repository.BankAccountRepository;
import com.bank.app.domain.repository.LoanRepository;
import com.bank.app.domain.repository.UserRepository;
import com.bank.app.domain.service.LoanDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanUseCase implements LoanInputPort {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository accountRepository;
    private final LoanDomainService loanDomainService;
    private final AuditLogOutputPort auditLog;

    @Override
    @Transactional
    public LoanResponse requestLoan(CreateLoanRequest req) {
        userRepository.findByIdentificationNumber(req.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + req.getClientId()))
                .validateIsActive();

        Loan loan = Loan.builder()
                .loanType(req.getLoanType())
                .clientId(req.getClientId())
                .requestedAmount(Money.of(req.getRequestedAmount(), req.getCurrency()))
                .termMonths(req.getTermMonths())
                .status(LoanStatus.UNDER_REVIEW)
                .build();

        Loan saved = loanRepository.save(loan);
        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_REQUESTED")
                .operationDateTime(LocalDateTime.now())
                .affectedProductId(String.valueOf(saved.getId()))
                .details(Map.of("clientId", req.getClientId(), "requestedAmount", req.getRequestedAmount()))
                .build());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public LoanResponse approveLoan(Long loanId, ApproveLoanRequest req, Long analystId) {
        User analyst = userRepository.findById(analystId)
                .orElseThrow(() -> new ResourceNotFoundException("Analyst not found"));
        if (!analyst.hasRole(UserRole.INTERNAL_ANALYST))
            throw new UnauthorizedOperationException("Only Internal Analysts can approve loans");

        Loan loan = findLoan(loanId);
        loan.approve(analystId, Money.of(req.getApprovedAmount(), req.getCurrency()), req.getInterestRate());
        Loan saved = loanRepository.save(loan);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_APPROVED")
                .operationDateTime(LocalDateTime.now())
                .userId(analystId)
                .userRole(UserRole.INTERNAL_ANALYST.name())
                .affectedProductId(String.valueOf(loanId))
                .details(Map.of("approvedAmount", req.getApprovedAmount(), "interestRate", req.getInterestRate(),
                        "previousStatus", "UNDER_REVIEW", "newStatus", "APPROVED", "analystId", analystId))
                .build());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public LoanResponse rejectLoan(Long loanId, Long analystId) {
        User analyst = userRepository.findById(analystId)
                .orElseThrow(() -> new ResourceNotFoundException("Analyst not found"));
        if (!analyst.hasRole(UserRole.INTERNAL_ANALYST))
            throw new UnauthorizedOperationException("Only Internal Analysts can reject loans");

        Loan loan = findLoan(loanId);
        loan.reject(analystId);
        Loan saved = loanRepository.save(loan);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_REJECTED")
                .operationDateTime(LocalDateTime.now())
                .userId(analystId)
                .userRole(UserRole.INTERNAL_ANALYST.name())
                .affectedProductId(String.valueOf(loanId))
                .details(Map.of("previousStatus", "UNDER_REVIEW", "newStatus", "REJECTED", "analystId", analystId))
                .build());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public LoanResponse disburseLoan(Long loanId, DisburseLoanRequest req, Long analystId) {
        User analyst = userRepository.findById(analystId)
                .orElseThrow(() -> new ResourceNotFoundException("Analyst not found"));
        if (!analyst.hasRole(UserRole.INTERNAL_ANALYST))
            throw new UnauthorizedOperationException("Only Internal Analysts can disburse loans");

        Loan loan = findLoan(loanId);
        BankAccount account = accountRepository.findByAccountNumber(req.getDisbursementAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + req.getDisbursementAccountNumber()));

        loanDomainService.disburseLoanToAccount(loan, account);
        loanRepository.save(loan);
        accountRepository.save(account);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_DISBURSED")
                .operationDateTime(LocalDateTime.now())
                .userId(analystId)
                .userRole(UserRole.INTERNAL_ANALYST.name())
                .affectedProductId(String.valueOf(loanId))
                .details(Map.of("disbursedAmount", loan.getApprovedAmount().getAmount(),
                        "targetAccount", req.getDisbursementAccountNumber()))
                .build());
        return toResponse(loan);
    }

    @Override
    public LoanResponse getLoanById(Long loanId) {
        return toResponse(findLoan(loanId));
    }

    @Override
    public List<LoanResponse> getLoansByClient(String clientId) {
        return loanRepository.findByClientId(clientId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private Loan findLoan(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + id));
    }

    private LoanResponse toResponse(Loan l) {
        return LoanResponse.builder()
                .id(l.getId())
                .loanType(l.getLoanType())
                .clientId(l.getClientId())
                .requestedAmount(l.getRequestedAmount() != null ? l.getRequestedAmount().getAmount() : null)
                .approvedAmount(l.getApprovedAmount() != null ? l.getApprovedAmount().getAmount() : null)
                .currency(l.getRequestedAmount() != null ? l.getRequestedAmount().getCurrency() : null)
                .interestRate(l.getInterestRate())
                .termMonths(l.getTermMonths())
                .status(l.getStatus())
                .approvalDate(l.getApprovalDate())
                .disbursementDate(l.getDisbursementDate())
                .disbursementAccountNumber(l.getDisbursementAccountNumber())
                .build();
    }
}
