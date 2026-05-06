package com.bank.app.domain.exception;
public class InsufficientFundsException extends DomainException {
    public InsufficientFundsException(String message) { super(message); }
}
