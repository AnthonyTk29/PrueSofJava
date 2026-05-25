package com.banking.accounts.infrastructure.persistence;

import com.banking.accounts.infrastructure.persistence.jpa.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, Integer> {

    Optional<AccountJpaEntity> findByAccountNumber(String accountNumber);

    List<AccountJpaEntity> findByClientId(Integer clientId);

    boolean existsByAccountNumber(String accountNumber);
}
