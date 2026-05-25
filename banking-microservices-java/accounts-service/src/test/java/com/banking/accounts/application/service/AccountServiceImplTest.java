package com.banking.accounts.application.service;

import com.banking.accounts.application.dto.AccountCreateRequestDTO;
import com.banking.accounts.application.dto.AccountResponseDTO;
import com.banking.accounts.application.mapper.AccountMapper;
import com.banking.accounts.domain.entity.Account;
import com.banking.accounts.domain.exception.AccountNotFoundException;
import com.banking.accounts.domain.exception.ClientNotExistsException;
import com.banking.accounts.domain.exception.DuplicateAccountNumberException;
import com.banking.accounts.domain.exception.InvalidAccountDataException;
import com.banking.accounts.domain.repository.AccountRepository;
import com.banking.accounts.domain.repository.CustomerReadModelRepository;
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
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerReadModelRepository customerReadModelRepository;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepository, customerReadModelRepository, new AccountMapper());
    }

    @Test
    void create_success() {
        AccountCreateRequestDTO request = new AccountCreateRequestDTO(
                "478758", "SAVINGS", new BigDecimal("2000.00"), 1, true
        );
        Account saved = buildAccount(1, "478758", "SAVINGS", new BigDecimal("2000.00"), 1);

        when(customerReadModelRepository.existsByClientId(1)).thenReturn(true);
        when(accountRepository.existsByAccountNumber("478758")).thenReturn(false);
        when(accountRepository.save(any())).thenReturn(saved);

        AccountResponseDTO response = accountService.create(request);

        assertThat(response.accountNumber()).isEqualTo("478758");
        assertThat(response.accountType()).isEqualTo("SAVINGS");
        assertThat(response.initialBalance()).isEqualByComparingTo(new BigDecimal("2000.00"));
    }

    @Test
    void create_customerNotExists_throwsException() {
        AccountCreateRequestDTO request = new AccountCreateRequestDTO(
                "478758", "SAVINGS", new BigDecimal("2000.00"), 999, true
        );
        when(customerReadModelRepository.existsByClientId(999)).thenReturn(false);

        assertThatThrownBy(() -> accountService.create(request))
                .isInstanceOf(ClientNotExistsException.class)
                .hasMessageContaining("999");
    }

    @Test
    void create_accountNumberDuplicate_throwsException() {
        AccountCreateRequestDTO request = new AccountCreateRequestDTO(
                "478758", "SAVINGS", new BigDecimal("2000.00"), 1, true
        );
        when(customerReadModelRepository.existsByClientId(1)).thenReturn(true);
        when(accountRepository.existsByAccountNumber("478758")).thenReturn(true);

        assertThatThrownBy(() -> accountService.create(request))
                .isInstanceOf(DuplicateAccountNumberException.class)
                .hasMessageContaining("478758");
    }

    @Test
    void create_invalidAccountType_throwsException() {
        AccountCreateRequestDTO request = new AccountCreateRequestDTO(
                "478758", "INVALID", new BigDecimal("2000.00"), 1, true
        );
        when(customerReadModelRepository.existsByClientId(1)).thenReturn(true);
        when(accountRepository.existsByAccountNumber("478758")).thenReturn(false);

        assertThatThrownBy(() -> accountService.create(request))
                .isInstanceOf(InvalidAccountDataException.class)
                .hasMessageContaining("AHORRO");
    }

    @Test
    void getById_notFound_throwsException() {
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getById(999))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getAll_returnsAccounts() {
        Account a1 = buildAccount(1, "478758", "SAVINGS", new BigDecimal("2000"), 1);
        Account a2 = buildAccount(2, "225487", "CHECKING", new BigDecimal("100"), 2);
        when(accountRepository.findAll()).thenReturn(List.of(a1, a2));

        List<AccountResponseDTO> result = accountService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).accountNumber()).isEqualTo("478758");
        assertThat(result.get(1).accountNumber()).isEqualTo("225487");
    }

    @Test
    void patchStatus_deactivates() {
        Account account = buildAccount(1, "478758", "SAVINGS", new BigDecimal("2000"), 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AccountResponseDTO response = accountService.patchStatus(1, false);

        assertThat(response.active()).isFalse();
    }

    @Test
    void patchStatus_activates() {
        Account account = buildAccount(1, "478758", "SAVINGS", new BigDecimal("2000"), 1);
        account.deactivate();
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AccountResponseDTO response = accountService.patchStatus(1, true);

        assertThat(response.active()).isTrue();
    }

    @Test
    void delete_success() {
        Account account = buildAccount(1, "478758", "SAVINGS", new BigDecimal("2000"), 1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        AccountResponseDTO response = accountService.delete(1);

        assertThat(response.accountNumber()).isEqualTo("478758");
    }

    private Account buildAccount(Integer id, String accountNumber, String accountType, BigDecimal balance, Integer clientId) {
        Account account = new Account();
        try {
            var field = Account.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(account, id);
        } catch (Exception ignored) {}
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setInitialBalance(balance);
        account.setAvailableBalance(balance);
        account.setClientId(clientId);
        account.setActive(true);
        return account;
    }
}
