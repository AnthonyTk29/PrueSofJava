package com.banking.accounts.domain.exception;

public class InvalidTransactionDataException extends DomainException {

    public InvalidTransactionDataException(String message) {
        super(message);
    }
}
