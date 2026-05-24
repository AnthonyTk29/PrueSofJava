package com.banking.cuentas.domain.repository;

import com.banking.cuentas.domain.entity.Movimiento;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository {

    Optional<Movimiento> findById(Integer id);

    List<Movimiento> findAll();

    List<Movimiento> findByCuentaIdAndFechaBetween(Integer cuentaId, Instant inicio, Instant fin);

    Movimiento save(Movimiento movimiento);

    void deleteById(Integer id);
}
