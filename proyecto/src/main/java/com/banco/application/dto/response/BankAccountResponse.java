package com.banco.application.dto.response;

import com.banco.domain.model.valueobject.AccountStatus;
import com.banco.domain.model.valueobject.AccountType;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class BankAccountResponse {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private String ownerId;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private LocalDate openingDate;
}
