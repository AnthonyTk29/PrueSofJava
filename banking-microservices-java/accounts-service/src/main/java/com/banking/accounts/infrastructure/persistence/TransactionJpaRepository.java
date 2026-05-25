package com.banking.accounts.infrastructure.persistence;

import com.banking.accounts.infrastructure.persistence.jpa.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, Integer> {

    List<TransactionJpaEntity> findByAccountIdAndDateBetween(Integer accountId, Instant start, Instant end);
}
