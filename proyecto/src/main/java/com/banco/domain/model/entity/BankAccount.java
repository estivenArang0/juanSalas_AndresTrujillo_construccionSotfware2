package com.bank.app.domain.model.entity;

import com.bank.app.domain.exception.AccountOperationNotAllowedException;
import com.bank.app.domain.exception.InsufficientFundsException;
import com.bank.app.domain.model.valueobject.AccountStatus;
import com.bank.app.domain.model.valueobject.AccountType;
import com.bank.app.domain.model.valueobject.Money;
import lombok.*;
import java.time.LocalDate;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class BankAccount {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private String ownerId;
    private Money balance;
    private AccountStatus status;
    private LocalDate openingDate;

    public void validateIsOperational() {
        if (this.status == AccountStatus.BLOCKED || this.status == AccountStatus.CANCELLED)
            throw new AccountOperationNotAllowedException(
                "Operations not allowed on account " + accountNumber + ". Status: " + status);
    }

    public void credit(Money amount) {
        // ✅ FIX 1: validar que la cuenta esté operacional antes de acreditar
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

    // ✅ FIX 2: null-check en balance para evitar NullPointerException
    public boolean hasSufficientFunds(Money amount) {
        return this.balance != null && this.balance.isGreaterThanOrEqual(amount);
    }

    public boolean isActive() { return AccountStatus.ACTIVE.equals(this.status); }

    // ✅ FIX 3: métodos de negocio como única vía para cambiar estado y balance
    // Se eliminó @Setter global — los campos solo se modifican por métodos controlados
    public void block()    { this.status = AccountStatus.BLOCKED; }
    public void cancel()   { this.status = AccountStatus.CANCELLED; }
    public void activate() { this.status = AccountStatus.ACTIVE; }
}