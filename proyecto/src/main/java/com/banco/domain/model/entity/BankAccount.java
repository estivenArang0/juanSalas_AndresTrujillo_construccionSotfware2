package com.banco.domain.model.entity;

import com.banco.domain.exception.AccountOperationNotAllowedException;
import com.banco.domain.exception.InsufficientFundsException;
import com.banco.domain.model.valueobject.AccountStatus;
import com.banco.domain.model.valueobject.AccountType;
import com.banco.domain.model.valueobject.Money;
import java.time.LocalDate;

public class BankAccount {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private String ownerId;
    private Money balance;
    private AccountStatus status;
    private LocalDate openingDate;

    public BankAccount() {}

    public BankAccount(Long id, String accountNumber, AccountType accountType, String ownerId, Money balance, AccountStatus status, LocalDate openingDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.ownerId = ownerId;
        this.balance = balance;
        this.status = status;
        this.openingDate = openingDate;
    }

    public static BankAccountBuilder builder() { return new BankAccountBuilder(); }

    public static class BankAccountBuilder {
        private BankAccount a = new BankAccount();
        public BankAccountBuilder id(Long id) { a.id = id; return this; }
        public BankAccountBuilder accountNumber(String num) { a.accountNumber = num; return this; }
        public BankAccountBuilder accountType(AccountType type) { a.accountType = type; return this; }
        public BankAccountBuilder ownerId(String id) { a.ownerId = id; return this; }
        public BankAccountBuilder holderId(String id) { a.ownerId = id; return this; } // Alias
        public BankAccountBuilder balance(Money m) { a.balance = m; return this; }
        public BankAccountBuilder currentBalance(java.math.BigDecimal b) { a.balance = Money.of(b, "COP"); return this; } // Alias
        public BankAccountBuilder status(AccountStatus s) { a.status = s; return this; }
        public BankAccountBuilder accountStatus(AccountStatus s) { a.status = s; return this; } // Alias
        public BankAccountBuilder openingDate(LocalDate date) { a.openingDate = date; return this; }
        public BankAccount build() { return a; }
    }

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public AccountType getAccountType() { return accountType; }
    public String getOwnerId() { return ownerId; }
    public Money getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public LocalDate getOpeningDate() { return openingDate; }

    public void validateIsOperational() {
        if (this.status == AccountStatus.BLOCKED || this.status == AccountStatus.CANCELLED)
            throw new AccountOperationNotAllowedException(
                "Operations not allowed on account " + accountNumber + ". Status: " + status);
    }

    public void credit(Money amount) {
        validateIsOperational();
        if (!amount.isPositive()) throw new IllegalArgumentException("Credit amount must be positive");
        if (this.balance == null) {
            this.balance = Money.zero(amount.getCurrency());
        }
        this.balance = this.balance.add(amount);
    }

    public void debit(Money amount) {
        validateIsOperational();
        if (!amount.isPositive()) throw new IllegalArgumentException("Debit amount must be positive");
        if (this.balance == null)
            throw new InsufficientFundsException(
                "Insufficient funds in " + accountNumber + ". Balance: null, Required: " + amount);
        if (!this.balance.isGreaterThanOrEqual(amount))
            throw new InsufficientFundsException(
                "Insufficient funds in " + accountNumber + ". Balance: " + balance + ", Required: " + amount);
        this.balance = this.balance.subtract(amount);
    }

    public String getCurrency() {
        return this.balance != null ? this.balance.getCurrency() : null;
    }

    public boolean hasSufficientFunds(Money amount) {
        return this.balance != null && this.balance.isGreaterThanOrEqual(amount);
    }
    
    public boolean hasSufficientBalance(Money amount) {
        return this.balance != null && this.balance.isGreaterThanOrEqual(amount);
    }

    public boolean isActive() { return AccountStatus.ACTIVE.equals(this.status); }

    public void block()    { this.status = AccountStatus.BLOCKED; }
    public void cancel()   { this.status = AccountStatus.CANCELLED; }
    public void activate() { this.status = AccountStatus.ACTIVE; }
}
