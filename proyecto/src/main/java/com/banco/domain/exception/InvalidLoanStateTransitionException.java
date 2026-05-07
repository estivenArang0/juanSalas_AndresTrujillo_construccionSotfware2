package com.banco.domain.exception;
public class InvalidLoanStateTransitionException extends DomainException {
    public InvalidLoanStateTransitionException(String message) { super(message); }
}
