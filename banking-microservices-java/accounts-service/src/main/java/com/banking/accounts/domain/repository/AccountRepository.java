package com.banking.accounts.domain.repository;

import com.banking.accounts.domain.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(Integer id);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findAll();

    List<Account> findByClientId(Integer clientId);

    Account save(Account account);

    void deleteById(Integer id);

    boolean existsByAccountNumber(String accountNumber);
}
