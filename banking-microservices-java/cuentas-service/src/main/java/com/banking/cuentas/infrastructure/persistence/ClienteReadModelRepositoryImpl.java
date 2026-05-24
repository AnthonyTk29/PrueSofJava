package com.banking.cuentas.infrastructure.persistence;

import com.banking.cuentas.domain.entity.ClienteReadModel;
import com.banking.cuentas.domain.repository.ClienteReadModelRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClienteReadModelRepositoryImpl implements ClienteReadModelRepository {

    private final ClienteReadModelJpaRepository jpaRepository;

    public ClienteReadModelRepositoryImpl(ClienteReadModelJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<ClienteReadModel> findByClienteId(Integer clienteId) {
        return jpaRepository.findByClienteId(clienteId);
    }

    @Override
    public boolean existsByClienteId(Integer clienteId) {
        return jpaRepository.existsByClienteId(clienteId);
    }

    @Override
    public ClienteReadModel save(ClienteReadModel model) {
        return jpaRepository.save(model);
    }
}
