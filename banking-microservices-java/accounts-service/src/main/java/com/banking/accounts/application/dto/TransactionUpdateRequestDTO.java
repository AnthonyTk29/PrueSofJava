package com.banking.accounts.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionUpdateRequestDTO(
        @NotBlank(message = "El número de cuenta es obligatorio") String accountNumber,
        @NotBlank(message = "El tipo de transacción es obligatorio") String transactionType,
        @NotNull(message = "El monto es obligatorio") BigDecimal amount
) {}
