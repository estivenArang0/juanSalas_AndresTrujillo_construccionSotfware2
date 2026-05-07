package com.banco.application.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class DisburseLoanRequest {
    @NotBlank private String disbursementAccountNumber;
}
