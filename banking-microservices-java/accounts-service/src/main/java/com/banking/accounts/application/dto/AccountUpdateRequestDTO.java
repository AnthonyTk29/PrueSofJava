package com.banking.accounts.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountUpdateRequestDTO(
        @NotBlank(message = "El número de cuenta es obligatorio") String accountNumber,
        @NotBlank(message = "El tipo de cuenta es obligatorio") String accountType,
        @NotNull(message = "El ID del cliente es obligatorio") Integer clientId,
        @NotNull boolean active
) {}
