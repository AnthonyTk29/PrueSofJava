package com.banking.cuentas.infrastructure.persistence;

import com.banking.cuentas.domain.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MovimientoJpaRepository extends JpaRepository<Movimiento, Integer> {

    List<Movimiento> findByCuentaIdAndFechaBetween(Integer cuentaId, Instant inicio, Instant fin);
}
