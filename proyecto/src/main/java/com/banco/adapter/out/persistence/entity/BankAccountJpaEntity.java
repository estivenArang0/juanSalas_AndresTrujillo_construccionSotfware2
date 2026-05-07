package com.banco.adapter.out.persistence.entity;

import com.banco.domain.model.valueobject.AccountStatus;
import com.banco.domain.model.valueobject.AccountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "bank_accounts")
public class BankAccountJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_number", unique = true, nullable = false) private String accountNumber;
    @Enumerated(EnumType.STRING) @Column(name = "account_type", nullable = false) private AccountType accountType;
    @Column(name = "owner_id", nullable = false) private String ownerId;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal balance;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private AccountStatus status;
    @Column(name = "opening_date") private LocalDate openingDate;

    public BankAccountJpaEntity() {}

    public BankAccountJpaEntity(Long id, String accountNumber, AccountType accountType, String ownerId, BigDecimal balance, String currency, AccountStatus status, LocalDate openingDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.ownerId = ownerId;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
        this.openingDate = openingDate;
    }

    public static BankAccountJpaEntityBuilder builder() { return new BankAccountJpaEntityBuilder(); }

    public static class BankAccountJpaEntityBuilder {
        private BankAccountJpaEntity e = new BankAccountJpaEntity();
        public BankAccountJpaEntityBuilder id(Long id) { e.id = id; return this; }
        public BankAccountJpaEntityBuilder accountNumber(String n) { e.accountNumber = n; return this; }
        public BankAccountJpaEntityBuilder accountType(AccountType t) { e.accountType = t; return this; }
        public BankAccountJpaEntityBuilder ownerId(String o) { e.ownerId = o; return this; }
        public BankAccountJpaEntityBuilder balance(BigDecimal b) { e.balance = b; return this; }
        public BankAccountJpaEntityBuilder currency(String c) { e.currency = c; return this; }
        public BankAccountJpaEntityBuilder status(AccountStatus s) { e.status = s; return this; }
        public BankAccountJpaEntityBuilder openingDate(LocalDate d) { e.openingDate = d; return this; }
        public BankAccountJpaEntity build() { return e; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }
}
