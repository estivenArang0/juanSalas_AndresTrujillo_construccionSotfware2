package com.bank.app.adapter.out.persistence.entity;

import com.bank.app.domain.model.valueobject.TransferStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "transfers")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
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
}
