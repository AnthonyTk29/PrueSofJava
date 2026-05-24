package com.banking.cuentas.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CuentaCreateRequest(
        @NotBlank(message = "El número de cuenta es requerido") String numeroCuenta,
        @NotBlank(message = "El tipo de cuenta es requerido") String tipoCuenta,
        @NotNull @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo") BigDecimal saldoInicial,
        @NotNull(message = "El clienteId es requerido") Integer clienteId,
        @NotNull boolean estado
) {}
