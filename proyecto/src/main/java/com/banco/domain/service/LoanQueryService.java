package com.banco.domain.service;

import com.banco.application.dto.response.LoanResponse;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.Loan;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.LoanRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanQueryService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public LoanResponse getLoanById(Long loanId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Préstamo no encontrado: " + loanId));

        // Clientes solo pueden ver sus propios préstamos
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) ||
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!loan.getClientId().equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar este préstamo");
            }
        }

        return toResponse(loan);
    }

    public List<LoanResponse> getLoansByClient(String clientId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Clientes solo pueden ver sus propios préstamos
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) ||
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!clientId.equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar los préstamos de otro cliente");
            }
        }

        return loanRepository.findByClientId(clientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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
