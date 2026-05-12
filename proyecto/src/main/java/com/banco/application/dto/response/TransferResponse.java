package com.banco.application.dto.response;
import com.banco.domain.model.valueobject.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class TransferResponse {
    private Long id;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime requestedAt;
    private LocalDateTime executedAt;
    private String description;
    private TransferStatus status;
    private Long creatorUserId;
    private Long approverUserId;
}

