package com.bank.app.application.dto.response;
import com.bank.app.domain.model.valueobject.AccountStatus;
import com.bank.app.domain.model.valueobject.AccountType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data @Builder
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
