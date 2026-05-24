package com.banking.cuentas.infrastructure.persistence;

import com.banking.cuentas.domain.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaJpaRepository extends JpaRepository<Cuenta, Integer> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    List<Cuenta> findByClienteId(Integer clienteId);

    boolean existsByNumeroCuenta(String numeroCuenta);
}
