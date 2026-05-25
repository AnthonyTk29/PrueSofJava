package com.banking.accounts.infrastructure.persistence;

import com.banking.accounts.infrastructure.persistence.jpa.CustomerReadModelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerReadModelJpaRepository extends JpaRepository<CustomerReadModelJpaEntity, Integer> {

    Optional<CustomerReadModelJpaEntity> findByClientId(Integer clientId);

    boolean existsByClientId(Integer clientId);
}
