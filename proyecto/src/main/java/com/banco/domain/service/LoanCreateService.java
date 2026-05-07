package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.CreateLoanRequest;
import com.banco.application.dto.response.LoanResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.exception.UserNotActiveException;
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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoanCreateService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public LoanResponse create(CreateLoanRequest request, Long requestingUserId) {

        // Solo empleados comerciales pueden crear solicitudes de préstamo
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE) &&
            !requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados comerciales o clientes pueden solicitar préstamos");
        }

        // Verificar que el cliente existe y está activo
        User client = userRepository.findByIdentificationNumber(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con identificación: " + request.getClientId()));

        if (!isUserEligible(client)) {
            throw new UserNotActiveException(
                    "El cliente " + request.getClientId() + " no está activo o no es elegible para un préstamo");
        }

        // Validar monto y plazo
        if (request.getRequestedAmount() == null || request.getRequestedAmount().signum() <= 0) {
            throw new IllegalArgumentException("El monto solicitado debe ser mayor a cero");
        }

        if (request.getTermMonths() == null || request.getTermMonths() <= 0) {
            throw new IllegalArgumentException("El plazo en meses debe ser mayor a cero");
        }

        // Verificar que el cliente no tenga préstamos activos pendientes de desembolso
        List<Loan> activeLoans = loanRepository.findByClientId(request.getClientId());
        boolean hasPendingLoan = activeLoans.stream()
                .anyMatch(l -> l.getStatus() == LoanStatus.UNDER_REVIEW ||
                               l.getStatus() == LoanStatus.APPROVED);

        if (hasPendingLoan) {
            throw new UnauthorizedOperationException(
                    "El cliente ya tiene un préstamo en estudio o aprobado pendiente de desembolso");
        }

        Loan newLoan = Loan.builder()
                .loanType(request.getLoanType())
                .clientId(request.getClientId())
                .requestedAmount(Money.of(request.getRequestedAmount(), request.getCurrency()))
                .termMonths(request.getTermMonths())
                .status(LoanStatus.UNDER_REVIEW)
                .build();

        Loan saved = loanRepository.save(newLoan);

        auditLog.log(AuditLogRequest.builder()
                .operationType("LOAN_CREATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(String.valueOf(saved.getId()))
                .details(Map.of(
                        "loanId", saved.getId(),
                        "clientId", request.getClientId(),
                        "loanType", request.getLoanType(),
                        "requestedAmount", request.getRequestedAmount(),
                        "termMonths", request.getTermMonths(),
                        "initialStatus", LoanStatus.UNDER_REVIEW.name()
                ))
                .build());

        return toResponse(saved);
    }

    private boolean isUserEligible(User client) {
        return client.isActive();
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