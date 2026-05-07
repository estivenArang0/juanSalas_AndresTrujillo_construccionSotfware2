package com.banco.domain.model.entity;

import com.banco.domain.exception.InvalidLoanStateTransitionException;
import com.banco.domain.model.valueobject.LoanStatus;
import com.banco.domain.model.valueobject.Money;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Loan {
    private Long id;
    private String loanType;
    private String clientId;
    private Money requestedAmount;
    private Money approvedAmount;
    private BigDecimal interestRate;
    private Integer termMonths;
    private LoanStatus status;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private String disbursementAccountNumber;
    private Long analystId;
    private LocalDate rejectionDate;

    public Loan() {}

    public Loan(Long id, String loanType, String clientId, Money requestedAmount, Money approvedAmount, BigDecimal interestRate, Integer termMonths, LoanStatus status, LocalDate approvalDate, LocalDate disbursementDate, String disbursementAccountNumber, Long analystId, LocalDate rejectionDate) {
        this.id = id;
        this.loanType = loanType;
        this.clientId = clientId;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.status = status;
        this.approvalDate = approvalDate;
        this.disbursementDate = disbursementDate;
        this.disbursementAccountNumber = disbursementAccountNumber;
        this.analystId = analystId;
        this.rejectionDate = rejectionDate;
    }

    public static LoanBuilder builder() { return new LoanBuilder(); }

    public static class LoanBuilder {
        private Loan l = new Loan();
        public LoanBuilder id(Long id) { l.id = id; return this; }
        public LoanBuilder loanType(String t) { l.loanType = t; return this; }
        public LoanBuilder clientId(String c) { l.clientId = c; return this; }
        public LoanBuilder requestedAmount(Money a) { l.requestedAmount = a; return this; }
        public LoanBuilder approvedAmount(Money a) { l.approvedAmount = a; return this; }
        public LoanBuilder interestRate(BigDecimal r) { l.interestRate = r; return this; }
        public LoanBuilder termMonths(Integer m) { l.termMonths = m; return this; }
        public LoanBuilder status(LoanStatus s) { l.status = s; return this; }
        public LoanBuilder approvalDate(LocalDate d) { l.approvalDate = d; return this; }
        public LoanBuilder disbursementDate(LocalDate d) { l.disbursementDate = d; return this; }
        public LoanBuilder disbursementAccountNumber(String n) { l.disbursementAccountNumber = n; return this; }
        public LoanBuilder analystId(Long a) { l.analystId = a; return this; }
        public LoanBuilder rejectionDate(LocalDate d) { l.rejectionDate = d; return this; }
        public Loan build() { return l; }
    }

    public Long getId() { return id; }
    public String getLoanType() { return loanType; }
    public String getClientId() { return clientId; }
    public Money getRequestedAmount() { return requestedAmount; }
    public Money getApprovedAmount() { return approvedAmount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public Integer getTermMonths() { return termMonths; }
    public LoanStatus getStatus() { return status; }
    public LocalDate getApprovalDate() { return approvalDate; }
    public LocalDate getDisbursementDate() { return disbursementDate; }
    public String getDisbursementAccountNumber() { return disbursementAccountNumber; }
    public Long getAnalystId() { return analystId; }
    public LocalDate getRejectionDate() { return rejectionDate; }

    public void approve(Long analystId, Money approvedAmount, BigDecimal interestRate) {
        if (this.status != LoanStatus.UNDER_REVIEW)
            throw new InvalidLoanStateTransitionException(
                "Loan can only be approved from UNDER_REVIEW. Current: " + status);
        this.status = LoanStatus.APPROVED;
        this.analystId = analystId;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.approvalDate = LocalDate.now();
    }

    public void reject(Long analystId) {
        if (this.status != LoanStatus.UNDER_REVIEW)
            throw new InvalidLoanStateTransitionException(
                "Loan can only be rejected from UNDER_REVIEW. Current: " + status);
        this.status = LoanStatus.REJECTED;
        this.analystId = analystId;
        this.rejectionDate = LocalDate.now();
    }

    public void disburse(String accountNumber) {
        if (this.status != LoanStatus.APPROVED)
            throw new InvalidLoanStateTransitionException(
                "Loan can only be disbursed from APPROVED. Current: " + status);
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Disbursement account number is required");
        if (approvedAmount == null || !approvedAmount.isPositive())
            throw new IllegalArgumentException("Approved amount must be greater than zero");

        this.status = LoanStatus.DISBURSED;
        this.disbursementAccountNumber = accountNumber;
        this.disbursementDate = LocalDate.now();
    }

    public boolean isDisbursable() {
        return LoanStatus.APPROVED.equals(this.status)
            && this.approvedAmount != null
            && this.approvedAmount.isPositive();
    }
}
