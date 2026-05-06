package com.bank.app.domain.exception;
public class UnauthorizedOperationException extends DomainException {
    public UnauthorizedOperationException(String message) { super(message); }
}
