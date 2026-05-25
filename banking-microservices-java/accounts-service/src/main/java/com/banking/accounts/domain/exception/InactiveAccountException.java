package com.banking.accounts.domain.exception;

public class InactiveAccountException extends DomainException {

    public InactiveAccountException(String message) {
        super(message);
    }
}
