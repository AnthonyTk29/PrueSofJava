package com.banking.cuentas.application.mapper;

import com.banking.cuentas.application.dto.CuentaCreateRequest;
import com.banking.cuentas.application.dto.CuentaResponse;
import com.banking.cuentas.application.dto.CuentaUpdateRequest;
import com.banking.cuentas.domain.entity.Cuenta;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.getClienteId(),
                cuenta.isEstado()
        );
    }

    public Cuenta toEntity(CuentaCreateRequest request) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldoInicial(request.saldoInicial());
        cuenta.setSaldoDisponible(request.saldoInicial());
        cuenta.setClienteId(request.clienteId());
        cuenta.setEstado(request.estado());
        return cuenta;
    }

    public void updateEntity(Cuenta cuenta, CuentaUpdateRequest request) {
        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldoInicial(request.saldoInicial());
        cuenta.setClienteId(request.clienteId());
        cuenta.setEstado(request.estado());
    }
}
