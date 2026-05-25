package com.banking.accounts.application.service;

import com.banking.accounts.application.dto.AccountCreateRequestDTO;
import com.banking.accounts.application.dto.AccountResponseDTO;
import com.banking.accounts.application.dto.AccountUpdateRequestDTO;

import java.util.List;

public interface AccountService {

    List<AccountResponseDTO> getAll();

    AccountResponseDTO getById(Integer id);

    AccountResponseDTO getByAccountNumber(String accountNumber);

    AccountResponseDTO create(AccountCreateRequestDTO request);

    AccountResponseDTO update(Integer id, AccountUpdateRequestDTO request);

    AccountResponseDTO patchStatus(Integer id, boolean active);

    AccountResponseDTO delete(Integer id);
}
