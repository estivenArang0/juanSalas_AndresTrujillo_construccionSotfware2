package com.bank.app.application.dto.response;
import com.bank.app.domain.model.valueobject.LoanStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data @Builder
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
