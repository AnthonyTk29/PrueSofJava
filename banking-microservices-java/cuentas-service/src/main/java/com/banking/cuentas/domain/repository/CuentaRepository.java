package com.banking.cuentas.domain.repository;

import com.banking.cuentas.domain.entity.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository {

    Optional<Cuenta> findById(Integer id);

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    List<Cuenta> findAll();

    List<Cuenta> findByClienteId(Integer clienteId);

    Cuenta save(Cuenta cuenta);

    void deleteById(Integer id);

    boolean existsByNumeroCuenta(String numeroCuenta);
}
