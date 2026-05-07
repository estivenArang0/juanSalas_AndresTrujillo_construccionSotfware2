package com.banco.adapter.out.persistence.entity;

import com.banco.domain.model.valueobject.TransferStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "transfers")
public class TransferJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "source_account_number", nullable = false) private String sourceAccountNumber;
    @Column(name = "destination_account_number", nullable = false) private String destinationAccountNumber;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal amount;
    @Column(nullable = false, length = 3) private String currency;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "approved_at") private LocalDateTime approvedAt;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TransferStatus status;
    @Column(name = "creator_user_id") private Long creatorUserId;
    @Column(name = "approver_user_id") private Long approverUserId;

    public TransferJpaEntity() {}

    public TransferJpaEntity(Long id, String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount, String currency, LocalDateTime createdAt, LocalDateTime approvedAt, TransferStatus status, Long creatorUserId, Long approverUserId) {
        this.id = id;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.status = status;
        this.creatorUserId = creatorUserId;
        this.approverUserId = approverUserId;
    }

    public static TransferJpaEntityBuilder builder() { return new TransferJpaEntityBuilder(); }

    public static class TransferJpaEntityBuilder {
        private TransferJpaEntity e = new TransferJpaEntity();
        public TransferJpaEntityBuilder id(Long id) { e.id = id; return this; }
        public TransferJpaEntityBuilder sourceAccountNumber(String s) { e.sourceAccountNumber = s; return this; }
        public TransferJpaEntityBuilder destinationAccountNumber(String d) { e.destinationAccountNumber = d; return this; }
        public TransferJpaEntityBuilder amount(BigDecimal a) { e.amount = a; return this; }
        public TransferJpaEntityBuilder currency(String c) { e.currency = c; return this; }
        public TransferJpaEntityBuilder createdAt(LocalDateTime c) { e.createdAt = c; return this; }
        public TransferJpaEntityBuilder approvedAt(LocalDateTime a) { e.approvedAt = a; return this; }
        public TransferJpaEntityBuilder status(TransferStatus s) { e.status = s; return this; }
        public TransferJpaEntityBuilder creatorUserId(Long c) { e.creatorUserId = c; return this; }
        public TransferJpaEntityBuilder approverUserId(Long a) { e.approverUserId = a; return this; }
        public TransferJpaEntity build() { return e; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }
    public Long getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(Long creatorUserId) { this.creatorUserId = creatorUserId; }
    public Long getApproverUserId() { return approverUserId; }
    public void setApproverUserId(Long approverUserId) { this.approverUserId = approverUserId; }
}
