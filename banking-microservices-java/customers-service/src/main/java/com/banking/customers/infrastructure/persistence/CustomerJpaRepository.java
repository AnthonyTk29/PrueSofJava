package com.banking.customers.infrastructure.persistence;

import com.banking.customers.infrastructure.persistence.jpa.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, Integer> {

    boolean existsByIdentification(String identification);
}
