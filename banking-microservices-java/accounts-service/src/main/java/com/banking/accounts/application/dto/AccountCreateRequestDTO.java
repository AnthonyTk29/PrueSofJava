package com.banking.accounts.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountCreateRequestDTO(
        @NotBlank(message = "El número de cuenta es obligatorio") String accountNumber,
        @NotBlank(message = "El tipo de cuenta es obligatorio") String accountType,
        @NotNull @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo") BigDecimal initialBalance,
        @NotNull(message = "El ID del cliente es obligatorio") Integer clientId,
        @NotNull boolean active
) {}
