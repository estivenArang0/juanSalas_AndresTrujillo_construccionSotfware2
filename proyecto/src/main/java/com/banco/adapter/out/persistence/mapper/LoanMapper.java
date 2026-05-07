package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.LoanJpaEntity;
import com.banco.domain.model.entity.Loan;
import com.banco.domain.model.valueobject.Money;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
    public Loan toDomain(LoanJpaEntity e) {
        if (e == null) return null;
        return Loan.builder()
                .id(e.getId())
                .loanType(e.getLoanType())
                .clientId(e.getClientId())
                .requestedAmount(Money.of(e.getRequestedAmount(), e.getCurrency()))
                .approvedAmount(e.getApprovedAmount() != null ? Money.of(e.getApprovedAmount(), e.getCurrency()) : null)
                .interestRate(e.getInterestRate())
                .termMonths(e.getTermMonths())
                .status(e.getStatus())
                .approvalDate(e.getApprovalDate())
                .disbursementDate(e.getDisbursementDate())
                .disbursementAccountNumber(e.getDisbursementAccountNumber())
                .analystId(e.getAnalystId())
                .build();
    }

    public LoanJpaEntity toEntity(Loan d) {
        if (d == null) return null;
        return LoanJpaEntity.builder()
                .id(d.getId())
                .loanType(d.getLoanType())
                .clientId(d.getClientId())
                .requestedAmount(d.getRequestedAmount() != null ? d.getRequestedAmount().getAmount() : null)
                .approvedAmount(d.getApprovedAmount() != null ? d.getApprovedAmount().getAmount() : null)
                .currency(d.getRequestedAmount() != null ? d.getRequestedAmount().getCurrency() : null)
                .interestRate(d.getInterestRate())
                .termMonths(d.getTermMonths())
                .status(d.getStatus())
                .approvalDate(d.getApprovalDate())
                .disbursementDate(d.getDisbursementDate())
                .disbursementAccountNumber(d.getDisbursementAccountNumber())
                .analystId(d.getAnalystId())
                .build();
    }
}
