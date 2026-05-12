package com.banco.application.dto.request;
import com.banco.domain.model.valueobject.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBankAccountRequest {
    @NotBlank private String ownerId;
    @NotNull private AccountType accountType;
    @NotBlank private String currency;


}
