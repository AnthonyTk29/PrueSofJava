package com.banking.cuentas.application.service;

import com.banking.cuentas.application.dto.ReporteResponse;

import java.time.LocalDate;

public interface ReporteService {

    ReporteResponse generate(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin);
}
