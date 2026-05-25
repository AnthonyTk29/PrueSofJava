package com.banking.accounts.infrastructure.persistence;

import com.banking.accounts.domain.entity.Transaction;
import com.banking.accounts.domain.repository.TransactionRepository;
import com.banking.accounts.infrastructure.persistence.jpa.TransactionJpaEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryImpl(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Transaction> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Transaction> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Transaction> findByAccountIdAndDateBetween(Integer accountId, Instant start, Instant end) {
        return jpaRepository.findByAccountIdAndDateBetween(accountId, start, end).stream()
                .map(this::toDomain).toList();
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity entity = toJpaEntity(transaction);
        TransactionJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    private Transaction toDomain(TransactionJpaEntity entity) {
        Transaction transaction = new Transaction();
        transaction.setId(entity.getId());
        transaction.setAccountId(entity.getAccountId());
        transaction.setAccountNumber(entity.getAccountNumber());
        transaction.setDate(entity.getDate());
        transaction.setTransactionType(entity.getTransactionType());
        transaction.setAmount(entity.getAmount());
        transaction.setBalance(entity.getBalance());
        return transaction;
    }

    private TransactionJpaEntity toJpaEntity(Transaction transaction) {
        TransactionJpaEntity entity = new TransactionJpaEntity();
        entity.setId(transaction.getId());
        entity.setAccountId(transaction.getAccountId());
        entity.setAccountNumber(transaction.getAccountNumber());
        entity.setDate(transaction.getDate());
        entity.setTransactionType(transaction.getTransactionType());
        entity.setAmount(transaction.getAmount());
        entity.setBalance(transaction.getBalance());
        return entity;
    }
}