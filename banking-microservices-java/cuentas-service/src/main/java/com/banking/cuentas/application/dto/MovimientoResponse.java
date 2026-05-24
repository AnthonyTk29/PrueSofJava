package com.banking.cuentas.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record MovimientoResponse(
        Integer id,
        String numeroCuenta,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo,
        Instant fecha
) {}
