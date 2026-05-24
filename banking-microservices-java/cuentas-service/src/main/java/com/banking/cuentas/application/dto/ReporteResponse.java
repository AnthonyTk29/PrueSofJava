package com.banking.cuentas.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ReporteResponse(
        Integer clienteId,
        String cliente,
        Instant fechaInicio,
        Instant fechaFin,
        List<CuentaReporte> cuentas
) {
    public record CuentaReporte(
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            BigDecimal saldoDisponible,
            boolean estado,
            List<MovimientoReporte> movimientos
    ) {}

    public record MovimientoReporte(
            Instant fecha,
            String tipoMovimiento,
            BigDecimal valor,
            BigDecimal saldo
    ) {}
}
