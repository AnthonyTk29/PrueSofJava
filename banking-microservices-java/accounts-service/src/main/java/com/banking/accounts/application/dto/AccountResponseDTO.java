package com.banking.accounts.application.dto;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Integer id,
        String accountNumber,
        String accountType,
        BigDecimal initialBalance,
        BigDecimal availableBalance,
        Integer clientId,
        boolean active
) {}
