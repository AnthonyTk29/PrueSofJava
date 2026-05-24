package com.banking.cuentas.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MovimientoUpdateRequest(
        @NotBlank(message = "El número de cuenta es requerido") String numeroCuenta,
        @NotBlank(message = "El tipo de movimiento es requerido") String tipoMovimiento,
        @NotNull(message = "El valor es requerido") BigDecimal valor
) {}
