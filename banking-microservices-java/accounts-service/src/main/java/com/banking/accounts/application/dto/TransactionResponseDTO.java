package com.banking.accounts.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDTO(
        Integer id,
        String accountNumber,
        String transactionType,
        BigDecimal amount,
        BigDecimal balance,
        Instant date
) {}
