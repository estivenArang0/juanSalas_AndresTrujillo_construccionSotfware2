package com.banco.application.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class CreateTransferRequest {
    @NotBlank private String sourceAccountNumber;
    @NotBlank private String destinationAccountNumber;
    @NotNull @Positive private BigDecimal amount;
    @NotBlank private String currency;
}
