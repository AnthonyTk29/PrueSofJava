package com.banking.cuentas.domain.repository;

import com.banking.cuentas.domain.entity.ClienteReadModel;

import java.util.Optional;

public interface ClienteReadModelRepository {

    Optional<ClienteReadModel> findByClienteId(Integer clienteId);

    boolean existsByClienteId(Integer clienteId);

    ClienteReadModel save(ClienteReadModel model);
}
