package com.banking.accounts.application.service;

import com.banking.accounts.application.dto.TransactionCreateRequestDTO;
import com.banking.accounts.application.dto.TransactionResponseDTO;
import com.banking.accounts.application.mapper.TransactionMapper;
import com.banking.accounts.domain.entity.Account;
import com.banking.accounts.domain.entity.Transaction;
import com.banking.accounts.domain.exception.AccountNotFoundException;
import com.banking.accounts.domain.exception.InactiveAccountException;
import com.banking.accounts.domain.exception.InsufficientBalanceException;
import com.banking.accounts.domain.repository.AccountRepository;
import com.banking.accounts.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(transactionRepository, accountRepository, new TransactionMapper());
    }

    @Test
    void createDeposit_success() {
        Account account = buildAccount("478758", new BigDecimal("2000.00"), true);
        TransactionCreateRequestDTO request = new TransactionCreateRequestDTO("478758", "DEPOSIT", new BigDecimal("500.00"));

        Transaction saved = Transaction.createDeposit(1, "478758", new BigDecimal("500.00"), new BigDecimal("2500.00"));
        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(saved);

        TransactionResponseDTO response = transactionService.create(request);

        assertThat(response.transactionType()).isEqualTo("DEPOSIT");
        assertThat(response.amount()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void createWithdrawal_success() {
        Account account = buildAccount("478758", new BigDecimal("2000.00"), true);
        TransactionCreateRequestDTO request = new TransactionCreateRequestDTO("478758", "WITHDRAWAL", new BigDecimal("-575.00"));

        Transaction saved = Transaction.createWithdrawal(1, "478758", new BigDecimal("-575.00"), new BigDecimal("1425.00"));
        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(saved);

        TransactionResponseDTO response = transactionService.create(request);

        assertThat(response.transactionType()).isEqualTo("WITHDRAWAL");
        assertThat(response.balance()).isEqualByComparingTo(new BigDecimal("1425.00"));
    }

    @Test
    void createWithdrawal_insufficientBalance_throwsException() {
        Account account = buildAccount("478758", new BigDecimal("100.00"), true);
        TransactionCreateRequestDTO request = new TransactionCreateRequestDTO("478758", "WITHDRAWAL", new BigDecimal("-500.00"));

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> transactionService.create(request))
                .isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    void create_inactiveAccount_throwsException() {
        Account account = buildAccount("478758", new BigDecimal("2000.00"), false);
        TransactionCreateRequestDTO request = new TransactionCreateRequestDTO("478758", "DEPOSIT", new BigDecimal("100.00"));

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> transactionService.create(request))
                .isInstanceOf(InactiveAccountException.class)
                .hasMessageContaining("inactiva");
    }

    @Test
    void create_accountNotFound_throwsException() {
        when(accountRepository.findByAccountNumber("000000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.create(
                new TransactionCreateRequestDTO("000000", "DEPOSIT", new BigDecimal("100.00"))))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getAll_returnsTransactions() {
        Transaction t = Transaction.createDeposit(1, "478758", new BigDecimal("100"), new BigDecimal("2100"));
        when(transactionRepository.findAll()).thenReturn(List.of(t));

        List<TransactionResponseDTO> result = transactionService.getAll();

        assertThat(result).hasSize(1);
    }

    private Account buildAccount(String accountNumber, BigDecimal balance, boolean active) {
        Account account = new Account();
        try {
            var field = Account.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(account, 1);
        } catch (Exception ignored) {}
        account.setAccountNumber(accountNumber);
        account.setAccountType("SAVINGS");
        account.setInitialBalance(balance);
        account.setAvailableBalance(balance);
        account.setClientId(1);
        account.setActive(active);
        return account;
    }
}
