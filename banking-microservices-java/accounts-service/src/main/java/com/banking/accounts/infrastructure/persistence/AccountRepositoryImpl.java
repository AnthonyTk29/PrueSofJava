package com.banking.accounts.infrastructure.persistence;

import com.banking.accounts.domain.entity.Account;
import com.banking.accounts.domain.repository.AccountRepository;
import com.banking.accounts.infrastructure.persistence.jpa.AccountJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository jpaRepository;

    public AccountRepositoryImpl(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jpaRepository.findByAccountNumber(accountNumber).map(this::toDomain);
    }

    @Override
    public List<Account> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Account> findByClientId(Integer clientId) {
        return jpaRepository.findByClientId(clientId).stream().map(this::toDomain).toList();
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = toJpaEntity(account);
        AccountJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return jpaRepository.existsByAccountNumber(accountNumber);
    }

    private Account toDomain(AccountJpaEntity entity) {
        Account account = new Account();
        account.setId(entity.getId());
        account.setAccountNumber(entity.getAccountNumber());
        account.setAccountType(entity.getAccountType());
        account.setInitialBalance(entity.getInitialBalance());
        account.setAvailableBalance(entity.getAvailableBalance());
        account.setActive(entity.isActive());
        account.setClientId(entity.getClientId());
        return account;
    }

    private AccountJpaEntity toJpaEntity(Account account) {
        AccountJpaEntity entity = new AccountJpaEntity();
        entity.setId(account.getId());
        entity.setAccountNumber(account.getAccountNumber());
        entity.setAccountType(account.getAccountType());
        entity.setInitialBalance(account.getInitialBalance());
        entity.setAvailableBalance(account.getAvailableBalance());
        entity.setActive(account.isActive());
        entity.setClientId(account.getClientId());
        return entity;
    }
}