package com.banco.application.dto.response;

import com.banco.domain.model.valueobject.AccountStatus;
import com.banco.domain.model.valueobject.AccountType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BankAccountResponse {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private String ownerId;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private LocalDate openingDate;

    public BankAccountResponse() {}

    public BankAccountResponse(Long id, String accountNumber, AccountType accountType, String ownerId, BigDecimal balance, String currency, AccountStatus status, LocalDate openingDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.ownerId = ownerId;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
        this.openingDate = openingDate;
    }

    public static BankAccountResponseBuilder builder() { return new BankAccountResponseBuilder(); }

    public static class BankAccountResponseBuilder {
        private BankAccountResponse r = new BankAccountResponse();
        public BankAccountResponseBuilder id(Long id) { r.id = id; return this; }
        public BankAccountResponseBuilder accountNumber(String n) { r.accountNumber = n; return this; }
        public BankAccountResponseBuilder accountType(AccountType t) { r.accountType = t; return this; }
        public BankAccountResponseBuilder ownerId(String o) { r.ownerId = o; return this; }
        public BankAccountResponseBuilder balance(BigDecimal b) { r.balance = b; return this; }
        public BankAccountResponseBuilder currency(String c) { r.currency = c; return this; }
        public BankAccountResponseBuilder status(AccountStatus s) { r.status = s; return this; }
        public BankAccountResponseBuilder openingDate(LocalDate d) { r.openingDate = d; return this; }
        public BankAccountResponse build() { return r; }
    }

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public AccountType getAccountType() { return accountType; }
    public String getOwnerId() { return ownerId; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public AccountStatus getStatus() { return status; }
    public LocalDate getOpeningDate() { return openingDate; }
}
