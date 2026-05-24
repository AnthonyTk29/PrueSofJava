package com.banking.cuentas.infrastructure.persistence;

import com.banking.cuentas.domain.entity.ClienteReadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteReadModelJpaRepository extends JpaRepository<ClienteReadModel, Integer> {

    Optional<ClienteReadModel> findByClienteId(Integer clienteId);

    boolean existsByClienteId(Integer clienteId);
}
