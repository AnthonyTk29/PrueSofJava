package com.banking.accounts.domain.exception;

public class DuplicateAccountNumberException extends DomainException {

    public DuplicateAccountNumberException(String message) {
        super(message);
    }
}
