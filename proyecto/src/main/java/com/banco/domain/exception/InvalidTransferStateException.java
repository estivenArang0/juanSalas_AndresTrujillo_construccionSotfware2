package com.bank.app.domain.exception;
public class InvalidTransferStateException extends DomainException {
    public InvalidTransferStateException(String message) { super(message); }
}
