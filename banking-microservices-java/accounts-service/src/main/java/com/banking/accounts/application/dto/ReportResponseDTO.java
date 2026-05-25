package com.banking.accounts.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ReportResponseDTO(
        Integer clientId,
        String clientName,
        Instant startDate,
        Instant endDate,
        List<AccountReport> accounts
) {
    public record AccountReport(
            String accountNumber,
            String accountType,
            BigDecimal initialBalance,
            BigDecimal availableBalance,
            boolean active,
            List<TransactionReport> transactions
    ) {}

    public record TransactionReport(
            Instant date,
            String transactionType,
            BigDecimal amount,
            BigDecimal balance
    ) {}
}
