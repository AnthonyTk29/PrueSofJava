package com.banking.accounts.domain.entity;

import com.banking.accounts.domain.exception.InvalidTransactionDataException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class Transaction {

    private Integer id;
    private Integer accountId;
    private String accountNumber;
    private Instant date;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal balance;

    public static Transaction createDeposit(Integer accountId, String accountNumber,
                                            BigDecimal amount, BigDecimal resultingBalance) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidTransactionDataException("El monto del depósito debe ser positivo");
        Transaction t = new Transaction();
        t.setAccountId(accountId);
        t.setAccountNumber(accountNumber);
        t.setTransactionType("DEPOSIT");
        t.setAmount(amount);
        t.setBalance(resultingBalance);
        t.setDate(Instant.now());
        return t;
    }

    public static Transaction createWithdrawal(Integer accountId, String accountNumber,
                                               BigDecimal amount, BigDecimal resultingBalance) {
        if (amount.compareTo(BigDecimal.ZERO) >= 0)
            throw new InvalidTransactionDataException("El monto del retiro debe ser negativo");
        Transaction t = new Transaction();
        t.setAccountId(accountId);
        t.setAccountNumber(accountNumber);
        t.setTransactionType("WITHDRAWAL");
        t.setAmount(amount);
        t.setBalance(resultingBalance);
        t.setDate(Instant.now());
        return t;
    }
}
