package com.banking.accounts.infrastructure.seed;

import com.banking.accounts.infrastructure.persistence.AccountJpaRepository;
import com.banking.accounts.infrastructure.persistence.CustomerReadModelJpaRepository;
import com.banking.accounts.infrastructure.persistence.TransactionJpaRepository;
import com.banking.accounts.infrastructure.persistence.jpa.AccountJpaEntity;
import com.banking.accounts.infrastructure.persistence.jpa.CustomerReadModelJpaEntity;
import com.banking.accounts.infrastructure.persistence.jpa.TransactionJpaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@Order(1)
public class AccountsSeeder implements ApplicationRunner {

    private final CustomerReadModelJpaRepository customerReadModelJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private final TransactionJpaRepository transactionJpaRepository;

    public AccountsSeeder(CustomerReadModelJpaRepository customerReadModelJpaRepository,
                           AccountJpaRepository accountJpaRepository,
                           TransactionJpaRepository transactionJpaRepository) {
        this.customerReadModelJpaRepository = customerReadModelJpaRepository;
        this.accountJpaRepository = accountJpaRepository;
        this.transactionJpaRepository = transactionJpaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (accountJpaRepository.count() == 0) {
            log.info("Seeding accounts data...");
            seedCustomersReadModel();
            List<AccountJpaEntity> accounts = seedAccounts();
            seedTransactions(accounts);
            log.info("Accounts seed data inserted.");
        }
    }

    private void seedCustomersReadModel() {
        CustomerReadModelJpaEntity c1 = new CustomerReadModelJpaEntity();
        c1.setClientId(1);
        c1.setName("Jose Lema");
        c1.setIdentification("1234567890");
        c1.setActive(true);

        CustomerReadModelJpaEntity c2 = new CustomerReadModelJpaEntity();
        c2.setClientId(2);
        c2.setName("Marianela Montalvo");
        c2.setIdentification("0987654321");
        c2.setActive(true);

        CustomerReadModelJpaEntity c3 = new CustomerReadModelJpaEntity();
        c3.setClientId(3);
        c3.setName("Juan Osorio");
        c3.setIdentification("1111111111");
        c3.setActive(true);

        customerReadModelJpaRepository.saveAll(List.of(c1, c2, c3));
    }

    private List<AccountJpaEntity> seedAccounts() {
        AccountJpaEntity a1 = buildAccount("478758", "SAVINGS", new BigDecimal("2000.00"), 1);
        AccountJpaEntity a2 = buildAccount("225487", "CHECKING", new BigDecimal("100.00"), 2);
        AccountJpaEntity a3 = buildAccount("495878", "SAVINGS", BigDecimal.ZERO, 3);
        AccountJpaEntity a4 = buildAccount("496825", "SAVINGS", new BigDecimal("540.00"), 2);
        AccountJpaEntity a5 = buildAccount("585545", "CHECKING", new BigDecimal("1000.00"), 3);
        return accountJpaRepository.saveAll(List.of(a1, a2, a3, a4, a5));
    }

    private void seedTransactions(List<AccountJpaEntity> accounts) {
        TransactionJpaEntity t1 = new TransactionJpaEntity();
        t1.setAccountId(accounts.get(0).getId());
        t1.setAccountNumber(accounts.get(0).getAccountNumber());
        t1.setDate(Instant.now());
        t1.setTransactionType("WITHDRAWAL");
        t1.setAmount(new BigDecimal("-575.00"));
        t1.setBalance(new BigDecimal("1425.00"));

        TransactionJpaEntity t2 = new TransactionJpaEntity();
        t2.setAccountId(accounts.get(1).getId());
        t2.setAccountNumber(accounts.get(1).getAccountNumber());
        t2.setDate(Instant.now());
        t2.setTransactionType("DEPOSIT");
        t2.setAmount(new BigDecimal("600.00"));
        t2.setBalance(new BigDecimal("700.00"));

        TransactionJpaEntity t3 = new TransactionJpaEntity();
        t3.setAccountId(accounts.get(2).getId());
        t3.setAccountNumber(accounts.get(2).getAccountNumber());
        t3.setDate(Instant.now());
        t3.setTransactionType("DEPOSIT");
        t3.setAmount(new BigDecimal("150.00"));
        t3.setBalance(new BigDecimal("150.00"));

        TransactionJpaEntity t4 = new TransactionJpaEntity();
        t4.setAccountId(accounts.get(3).getId());
        t4.setAccountNumber(accounts.get(3).getAccountNumber());
        t4.setDate(Instant.now());
        t4.setTransactionType("DEPOSIT");
        t4.setAmount(new BigDecimal("540.00"));
        t4.setBalance(new BigDecimal("540.00"));

        transactionJpaRepository.saveAll(List.of(t1, t2, t3, t4));
    }

    private AccountJpaEntity buildAccount(String accountNumber, String accountType, BigDecimal balance, Integer clientId) {
        AccountJpaEntity account = new AccountJpaEntity();
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setInitialBalance(balance);
        account.setAvailableBalance(balance);
        account.setClientId(clientId);
        account.setActive(true);
        return account;
    }
}