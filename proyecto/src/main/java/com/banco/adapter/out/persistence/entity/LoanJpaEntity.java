package com.banco.adapter.out.persistence.entity;

import com.banco.domain.model.valueobject.LoanStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "loans")
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

    public LoanJpaEntity() {}

    public LoanJpaEntity(Long id, String loanType, String clientId, BigDecimal requestedAmount, BigDecimal approvedAmount, String currency, BigDecimal interestRate, Integer termMonths, LoanStatus status, LocalDate approvalDate, LocalDate disbursementDate, String disbursementAccountNumber, Long analystId) {
        this.id = id;
        this.loanType = loanType;
        this.clientId = clientId;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.currency = currency;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.status = status;
        this.approvalDate = approvalDate;
        this.disbursementDate = disbursementDate;
        this.disbursementAccountNumber = disbursementAccountNumber;
        this.analystId = analystId;
    }

    public static LoanJpaEntityBuilder builder() { return new LoanJpaEntityBuilder(); }

    public static class LoanJpaEntityBuilder {
        private LoanJpaEntity e = new LoanJpaEntity();
        public LoanJpaEntityBuilder id(Long id) { e.id = id; return this; }
        public LoanJpaEntityBuilder loanType(String t) { e.loanType = t; return this; }
        public LoanJpaEntityBuilder clientId(String c) { e.clientId = c; return this; }
        public LoanJpaEntityBuilder requestedAmount(BigDecimal a) { e.requestedAmount = a; return this; }
        public LoanJpaEntityBuilder approvedAmount(BigDecimal a) { e.approvedAmount = a; return this; }
        public LoanJpaEntityBuilder currency(String c) { e.currency = c; return this; }
        public LoanJpaEntityBuilder interestRate(BigDecimal r) { e.interestRate = r; return this; }
        public LoanJpaEntityBuilder termMonths(Integer m) { e.termMonths = m; return this; }
        public LoanJpaEntityBuilder status(LoanStatus s) { e.status = s; return this; }
        public LoanJpaEntityBuilder approvalDate(LocalDate d) { e.approvalDate = d; return this; }
        public LoanJpaEntityBuilder disbursementDate(LocalDate d) { e.disbursementDate = d; return this; }
        public LoanJpaEntityBuilder disbursementAccountNumber(String n) { e.disbursementAccountNumber = n; return this; }
        public LoanJpaEntityBuilder analystId(Long a) { e.analystId = a; return this; }
        public LoanJpaEntity build() { return e; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }
    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }
    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate disbursementDate) { this.disbursementDate = disbursementDate; }
    public String getDisbursementAccountNumber() { return disbursementAccountNumber; }
    public void setDisbursementAccountNumber(String disbursementAccountNumber) { this.disbursementAccountNumber = disbursementAccountNumber; }
    public Long getAnalystId() { return analystId; }
    public void setAnalystId(Long analystId) { this.analystId = analystId; }
}
