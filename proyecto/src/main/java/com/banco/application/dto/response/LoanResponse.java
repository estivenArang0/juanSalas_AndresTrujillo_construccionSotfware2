package com.banco.application.dto.response;
import com.banco.domain.model.valueobject.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class LoanResponse {
    private Long id;
    private String loanType;
    private String clientId;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private String currency;
    private BigDecimal interestRate;
    private Integer termMonths;
    private LoanStatus status;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private String disbursementAccountNumber;
}

