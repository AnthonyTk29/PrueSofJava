package com.banking.accounts.domain.exception;

public class ClientNotExistsException extends DomainException {

    public ClientNotExistsException(String message) {
        super(message);
    }
}
