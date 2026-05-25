package com.banking.accounts.application.mapper;

import com.banking.accounts.application.dto.AccountCreateRequestDTO;
import com.banking.accounts.application.dto.AccountResponseDTO;
import com.banking.accounts.application.dto.AccountUpdateRequestDTO;
import com.banking.accounts.domain.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponseDTO toResponse(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getInitialBalance(),
                account.getAvailableBalance(),
                account.getClientId(),
                account.isActive()
        );
    }

    public Account toEntity(AccountCreateRequestDTO request) {
        Account account = new Account();
        account.setAccountNumber(request.accountNumber());
        account.setAccountType(request.accountType());
        account.setInitialBalance(request.initialBalance());
        account.setAvailableBalance(request.initialBalance());
        account.setClientId(request.clientId());
        account.setActive(request.active());
        return account;
    }

    public void updateEntity(Account account, AccountUpdateRequestDTO request) {
        account.setAccountNumber(request.accountNumber());
        account.setAccountType(request.accountType());
        account.setClientId(request.clientId());
        account.setActive(request.active());
    }
}
