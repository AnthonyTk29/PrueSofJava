package com.banking.cuentas.application.service;

import com.banking.cuentas.application.dto.CuentaCreateRequest;
import com.banking.cuentas.application.dto.CuentaResponse;
import com.banking.cuentas.application.dto.CuentaUpdateRequest;

import java.util.List;

public interface CuentaService {

    List<CuentaResponse> getAll();

    CuentaResponse getById(Integer id);

    CuentaResponse getByNumeroCuenta(String numeroCuenta);

    CuentaResponse create(CuentaCreateRequest request);

    CuentaResponse update(Integer id, CuentaUpdateRequest request);

    CuentaResponse patchEstado(Integer id, boolean estado);

    CuentaResponse delete(Integer id);
}
