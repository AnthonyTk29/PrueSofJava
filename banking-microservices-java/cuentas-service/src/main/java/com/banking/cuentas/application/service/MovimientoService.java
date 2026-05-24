package com.banking.cuentas.application.service;

import com.banking.cuentas.application.dto.MovimientoCreateRequest;
import com.banking.cuentas.application.dto.MovimientoResponse;
import com.banking.cuentas.application.dto.MovimientoUpdateRequest;

import java.util.List;

public interface MovimientoService {

    List<MovimientoResponse> getAll();

    MovimientoResponse getById(Integer id);

    MovimientoResponse create(MovimientoCreateRequest request);

    MovimientoResponse update(Integer id, MovimientoUpdateRequest request);

    MovimientoResponse delete(Integer id);
}
