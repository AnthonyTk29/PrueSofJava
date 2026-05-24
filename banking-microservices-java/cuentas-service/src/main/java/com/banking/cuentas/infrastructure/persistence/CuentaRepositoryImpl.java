package com.banking.cuentas.infrastructure.persistence;

import com.banking.cuentas.domain.entity.Cuenta;
import com.banking.cuentas.domain.repository.CuentaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CuentaRepositoryImpl implements CuentaRepository {

    private final CuentaJpaRepository jpaRepository;

    public CuentaRepositoryImpl(CuentaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Cuenta> findById(Integer id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        return jpaRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public List<Cuenta> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Cuenta> findByClienteId(Integer clienteId) {
        return jpaRepository.findByClienteId(clienteId);
    }

    @Override
    public Cuenta save(Cuenta cuenta) {
        return jpaRepository.save(cuenta);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNumeroCuenta(String numeroCuenta) {
        return jpaRepository.existsByNumeroCuenta(numeroCuenta);
    }
}
