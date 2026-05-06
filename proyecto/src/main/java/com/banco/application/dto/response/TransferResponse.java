package com.bank.app.application.dto.response;
import com.bank.app.domain.model.valueobject.TransferStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data @Builder
public class TransferResponse {
    private Long id;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private TransferStatus status;
    private Long creatorUserId;
    private Long approverUserId;
}
