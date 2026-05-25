package com.banking.accounts.infrastructure.persistence;

import com.banking.accounts.domain.entity.CustomerReadModel;
import com.banking.accounts.domain.repository.CustomerReadModelRepository;
import com.banking.accounts.infrastructure.persistence.jpa.CustomerReadModelJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerReadModelRepositoryImpl implements CustomerReadModelRepository {

    private final CustomerReadModelJpaRepository jpaRepository;

    public CustomerReadModelRepositoryImpl(CustomerReadModelJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<CustomerReadModel> findByClientId(Integer clientId) {
        return jpaRepository.findByClientId(clientId).map(this::toDomain);
    }

    @Override
    public boolean existsByClientId(Integer clientId) {
        return jpaRepository.existsByClientId(clientId);
    }

    @Override
    public CustomerReadModel save(CustomerReadModel model) {
        CustomerReadModelJpaEntity entity = toJpaEntity(model);
        CustomerReadModelJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    private CustomerReadModel toDomain(CustomerReadModelJpaEntity entity) {
        CustomerReadModel model = new CustomerReadModel();
        model.setId(entity.getId());
        model.setClientId(entity.getClientId());
        model.setName(entity.getName());
        model.setIdentification(entity.getIdentification());
        model.setActive(entity.isActive());
        return model;
    }

    private CustomerReadModelJpaEntity toJpaEntity(CustomerReadModel model) {
        CustomerReadModelJpaEntity entity = new CustomerReadModelJpaEntity();
        entity.setId(model.getId());
        entity.setClientId(model.getClientId());
        entity.setName(model.getName());
        entity.setIdentification(model.getIdentification());
        entity.setActive(model.isActive());
        return entity;
    }
}