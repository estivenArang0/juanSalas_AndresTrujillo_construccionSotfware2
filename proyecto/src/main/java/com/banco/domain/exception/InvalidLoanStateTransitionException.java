package com.bank.app.domain.exception;
public class InvalidLoanStateTransitionException extends DomainException {
    public InvalidLoanStateTransitionException(String message) { super(message); }
}
