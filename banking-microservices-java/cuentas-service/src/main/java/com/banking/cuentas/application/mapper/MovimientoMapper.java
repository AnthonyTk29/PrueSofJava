package com.banking.cuentas.application.mapper;

import com.banking.cuentas.application.dto.MovimientoResponse;
import com.banking.cuentas.domain.entity.Movimiento;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

    public MovimientoResponse toResponse(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getNumeroCuenta(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo(),
                movimiento.getFecha()
        );
    }
}
