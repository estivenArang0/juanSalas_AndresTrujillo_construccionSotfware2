package com.bank.app.adapter.out.persistence.entity;

import com.bank.app.domain.model.valueobject.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "loans")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class LoanJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "loan_type") private String loanType;
    @Column(name = "client_id", nullable = false) private String clientId;
    @Column(name = "requested_amount", nullable = false, precision = 19, scale = 4) private BigDecimal requestedAmount;
    @Column(name = "approved_amount", precision = 19, scale = 4) private BigDecimal approvedAmount;
    @Column(name = "currency", length = 3) private String currency;
    @Column(name = "interest_rate", precision = 5, scale = 4) private BigDecimal interestRate;
    @Column(name = "term_months") private Integer termMonths;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private LoanStatus status;
    @Column(name = "approval_date") private LocalDate approvalDate;
    @Column(name = "disbursement_date") private LocalDate disbursementDate;
    @Column(name = "disbursement_account_number") private String disbursementAccountNumber;
    @Column(name = "analyst_id") private Long analystId;
}
