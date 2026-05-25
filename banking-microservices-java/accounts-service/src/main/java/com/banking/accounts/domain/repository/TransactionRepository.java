package com.banking.accounts.domain.repository;

import com.banking.accounts.domain.entity.Transaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findById(Integer id);

    List<Transaction> findAll();

    List<Transaction> findByAccountIdAndDateBetween(Integer accountId, Instant start, Instant end);

    Transaction save(Transaction transaction);

    void deleteById(Integer id);
}
