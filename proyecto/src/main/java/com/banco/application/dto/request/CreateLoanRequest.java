package com.bank.app.application.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class CreateLoanRequest {
    @NotBlank private String clientId;
    @NotBlank private String loanType;
    @NotNull @Positive private BigDecimal requestedAmount;
    @NotBlank private String currency;
    @NotNull @Positive private Integer termMonths;
}
