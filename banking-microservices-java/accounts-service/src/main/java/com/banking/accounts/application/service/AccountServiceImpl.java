package com.banking.accounts.application.service;

import com.banking.accounts.application.dto.AccountCreateRequestDTO;
import com.banking.accounts.application.dto.AccountResponseDTO;
import com.banking.accounts.application.dto.AccountUpdateRequestDTO;
import com.banking.accounts.application.mapper.AccountMapper;
import com.banking.accounts.domain.entity.Account;
import com.banking.accounts.domain.exception.AccountNotFoundException;
import com.banking.accounts.domain.exception.ClientNotExistsException;
import com.banking.accounts.domain.exception.DuplicateAccountNumberException;
import com.banking.accounts.domain.repository.AccountRepository;
import com.banking.accounts.domain.repository.CustomerReadModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerReadModelRepository customerReadModelRepository;
    private final AccountMapper mapper;

    public AccountServiceImpl(AccountRepository accountRepository,
                               CustomerReadModelRepository customerReadModelRepository,
                               AccountMapper mapper) {
        this.accountRepository = accountRepository;
        this.customerReadModelRepository = customerReadModelRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAll() {
        return accountRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getById(Integer id) {
        return accountRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta con Id " + id + " no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(mapper::toResponse)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta con número " + accountNumber + " no encontrada"));
    }

    @Override
    @Transactional
    public AccountResponseDTO create(AccountCreateRequestDTO request) {
        if (!customerReadModelRepository.existsByClientId(request.clientId()))
            throw new ClientNotExistsException("El cliente con Id " + request.clientId() + " no existe");
        if (accountRepository.existsByAccountNumber(request.accountNumber()))
            throw new DuplicateAccountNumberException("El número de cuenta " + request.accountNumber() + " ya existe");
        Account account = mapper.toEntity(request);
        return mapper.toResponse(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountResponseDTO update(Integer id, AccountUpdateRequestDTO request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta con Id " + id + " no encontrada"));
        mapper.updateEntity(account, request);
        return mapper.toResponse(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountResponseDTO patchStatus(Integer id, boolean active) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta con Id " + id + " no encontrada"));
        if (active) account.activate();
        else account.deactivate();
        return mapper.toResponse(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountResponseDTO delete(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta con Id " + id + " no encontrada"));
        AccountResponseDTO response = mapper.toResponse(account);
        accountRepository.deleteById(id);
        return response;
    }
}
