package com.banking.cuentas.infrastructure.persistence;

import com.banking.cuentas.domain.entity.Movimiento;
import com.banking.cuentas.domain.repository.MovimientoRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class MovimientoRepositoryImpl implements MovimientoRepository {

    private final MovimientoJpaRepository jpaRepository;

    public MovimientoRepositoryImpl(MovimientoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Movimiento> findById(Integer id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Movimiento> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Movimiento> findByCuentaIdAndFechaBetween(Integer cuentaId, Instant inicio, Instant fin) {
        return jpaRepository.findByCuentaIdAndFechaBetween(cuentaId, inicio, fin);
    }

    @Override
    public Movimiento save(Movimiento movimiento) {
        return jpaRepository.save(movimiento);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
}
