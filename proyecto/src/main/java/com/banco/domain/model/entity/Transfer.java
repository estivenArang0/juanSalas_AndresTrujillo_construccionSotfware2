package com.banco.domain.model.entity;

import com.banco.domain.exception.InvalidTransferStateException;
import com.banco.domain.model.valueobject.Money;
import com.banco.domain.model.valueobject.TransferStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor @NoArgsConstructor
public class Transfer {
    public static TransferBuilder builder() { return new TransferBuilder(); }
    public static class TransferBuilder {
        private Transfer t = new Transfer();
        public TransferBuilder id(Long id) { t.id = id; return this; }
        public TransferBuilder sourceAccountNumber(String s) { t.sourceAccountNumber = s; return this; }
        public TransferBuilder destinationAccountNumber(String d) { t.destinationAccountNumber = d; return this; }
        public TransferBuilder amount(Money a) { t.amount = a; return this; }
        public TransferBuilder createdAt(LocalDateTime c) { t.createdAt = c; return this; }
        public TransferBuilder approvedAt(LocalDateTime a) { t.approvedAt = a; return this; }
        public TransferBuilder expiredAt(LocalDateTime e) { t.expiredAt = e; return this; }
        public TransferBuilder status(TransferStatus s) { t.status = s; return this; }
        public TransferBuilder creatorUserId(Long c) { t.creatorUserId = c; return this; }
        public TransferBuilder approverUserId(Long a) { t.approverUserId = a; return this; }
        public Transfer build() { return t; }
    }

    private Long id;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private Money amount;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime expiredAt;
    private TransferStatus status;
    private Long creatorUserId;
    private Long approverUserId;

    public Long getId() { return id; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public Money getAmount() { return amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public LocalDateTime getExpiredAt() { return expiredAt; }
    public TransferStatus getStatus() { return status; }
    public Long getCreatorUserId() { return creatorUserId; }
    public Long getApproverUserId() { return approverUserId; }

    // ✅ FIX 2: factory method para transferencia que requiere aprobación
    public static Transfer createPendingApproval(String sourceAccount, String destinationAccount,
                                                  Money amount, Long creatorUserId) {
        validateAmount(amount);
        return Transfer.builder()
                .sourceAccountNumber(sourceAccount)
                .destinationAccountNumber(destinationAccount)
                .amount(amount)
                .creatorUserId(creatorUserId)
                .createdAt(LocalDateTime.now())
                .status(TransferStatus.PENDING_APPROVAL)
                .build();
    }

    // ✅ FIX 2: factory method para transferencia directa (bajo monto)
    public static Transfer createDirect(String sourceAccount, String destinationAccount,
                                         Money amount, Long creatorUserId) {
        validateAmount(amount);
        return Transfer.builder()
                .sourceAccountNumber(sourceAccount)
                .destinationAccountNumber(destinationAccount)
                .amount(amount)
                .creatorUserId(creatorUserId)
                .createdAt(LocalDateTime.now())
                .status(TransferStatus.EXECUTED)
                .approvedAt(LocalDateTime.now())
                .build();
    }

    public void execute(Long approverId) {
        if (this.status != TransferStatus.PENDING_APPROVAL)
            throw new InvalidTransferStateException(
                "Transfer can only be executed from PENDING_APPROVAL. Current: " + status);
        // ✅ FIX 1: validación de fondos debe hacerse en el caso de uso antes de llamar este método
        this.status = TransferStatus.EXECUTED;
        this.approverUserId = approverId;
        this.approvedAt = LocalDateTime.now();
    }

    public void reject(Long approverId) {
        if (this.status != TransferStatus.PENDING_APPROVAL)
            throw new InvalidTransferStateException(
                "Transfer can only be rejected from PENDING_APPROVAL. Current: " + status);
        this.status = TransferStatus.REJECTED;
        this.approverUserId = approverId;
        this.approvedAt = LocalDateTime.now();
    }

    public void expire() {
        if (this.status != TransferStatus.PENDING_APPROVAL)
            throw new InvalidTransferStateException(
                "Transfer can only expire from PENDING_APPROVAL. Current: " + status);
        this.status = TransferStatus.EXPIRED;
        // ✅ FIX 3: registrar fecha exacta de vencimiento para bitácora
        this.expiredAt = LocalDateTime.now();
    }

    public boolean isExpired(int expirationMinutes) {
        return this.status == TransferStatus.PENDING_APPROVAL &&
               LocalDateTime.now().isAfter(this.createdAt.plusMinutes(expirationMinutes));
    }

    // ✅ FIX 5: validación de monto mayor a cero
    private static void validateAmount(Money amount) {
        if (amount == null || !amount.isPositive())
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
    }
}
