package com.banco.application.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class ApproveLoanRequest {
    @NotNull @Positive private BigDecimal approvedAmount;
    @NotBlank private String currency;
    @NotNull @Positive private BigDecimal interestRate;
}
