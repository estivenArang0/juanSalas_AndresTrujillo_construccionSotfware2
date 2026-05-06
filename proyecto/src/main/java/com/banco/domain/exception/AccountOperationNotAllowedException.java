package com.bank.app.domain.exception;
public class AccountOperationNotAllowedException extends DomainException {
    public AccountOperationNotAllowedException(String message) { super(message); }
}
