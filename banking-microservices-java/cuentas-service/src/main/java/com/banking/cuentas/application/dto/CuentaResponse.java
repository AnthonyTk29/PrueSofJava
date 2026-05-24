package com.banking.cuentas.application.dto;

import java.math.BigDecimal;

public record CuentaResponse(
        Integer id,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Integer clienteId,
        boolean estado
) {}
