package com.banking.accounts.domain.entity;

import com.banking.accounts.domain.exception.InvalidAccountDataException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Account {

    private Integer id;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private BigDecimal availableBalance;
    private boolean active = true;
    private Integer clientId;

    public void setAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank())
            throw new InvalidAccountDataException("El número de cuenta es obligatorio");
        this.accountNumber = accountNumber;
    }

    public void setAccountType(String accountType) {
        if (accountType == null || accountType.isBlank())
            throw new InvalidAccountDataException("El tipo de cuenta es obligatorio");
        String normalized = accountType.toUpperCase();
        if (!normalized.equals("SAVINGS") && !normalized.equals("CHECKING"))
            throw new InvalidAccountDataException("El tipo de cuenta debe ser AHORRO o CORRIENTE");
        this.accountType = normalized;
    }

    public void updateBalance(BigDecimal newBalance) {
        this.availableBalance = newBalance;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
