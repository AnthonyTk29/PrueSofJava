package com.banking.accounts.domain.repository;

import com.banking.accounts.domain.entity.CustomerReadModel;

import java.util.Optional;

public interface CustomerReadModelRepository {

    Optional<CustomerReadModel> findByClientId(Integer clientId);

    boolean existsByClientId(Integer clientId);

    CustomerReadModel save(CustomerReadModel model);
}
