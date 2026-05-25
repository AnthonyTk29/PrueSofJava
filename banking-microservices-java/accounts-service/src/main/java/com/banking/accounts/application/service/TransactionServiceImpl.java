package com.banking.accounts.application.service;

import com.banking.accounts.application.dto.TransactionCreateRequestDTO;
import com.banking.accounts.application.dto.TransactionResponseDTO;
import com.banking.accounts.application.dto.TransactionUpdateRequestDTO;
import com.banking.accounts.application.mapper.TransactionMapper;
import com.banking.accounts.domain.entity.Account;
import com.banking.accounts.domain.entity.Transaction;
import com.banking.accounts.domain.exception.AccountNotFoundException;
import com.banking.accounts.domain.exception.InactiveAccountException;
import com.banking.accounts.domain.exception.InsufficientBalanceException;
import com.banking.accounts.domain.exception.TransactionNotFoundException;
import com.banking.accounts.domain.repository.AccountRepository;
import com.banking.accounts.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper mapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                   AccountRepository accountRepository,
                                   TransactionMapper mapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getAll() {
        return transactionRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDTO getById(Integer id) {
        return transactionRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new TransactionNotFoundException("Movimiento con Id " + id + " no encontrado"));
    }

    @Override
    @Transactional
    public TransactionResponseDTO create(TransactionCreateRequestDTO request) {
        Account account = accountRepository.findByAccountNumber(request.accountNumber())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Cuenta con número " + request.accountNumber() + " no encontrada"));

        if (!account.isActive())
            throw new InactiveAccountException("La cuenta se encuentra inactiva");

        BigDecimal newBalance = account.getAvailableBalance().add(request.amount());

        if (request.amount().compareTo(BigDecimal.ZERO) < 0 && newBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientBalanceException("Saldo no disponible para realizar el retiro");

        Transaction transaction;
        if (request.amount().compareTo(BigDecimal.ZERO) >= 0) {
            transaction = Transaction.createDeposit(account.getId(), account.getAccountNumber(),
                    request.amount(), newBalance);
        } else {
            transaction = Transaction.createWithdrawal(account.getId(), account.getAccountNumber(),
                    request.amount(), newBalance);
        }

        account.updateBalance(newBalance);
        accountRepository.save(account);
        Transaction saved = transactionRepository.save(transaction);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TransactionResponseDTO update(Integer id, TransactionUpdateRequestDTO request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Movimiento con Id " + id + " no encontrado"));
        transaction.setAccountNumber(request.accountNumber());
        transaction.setTransactionType(request.transactionType().toUpperCase());
        transaction.setAmount(request.amount());
        return mapper.toResponse(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionResponseDTO delete(Integer id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Movimiento con Id " + id + " no encontrado"));
        TransactionResponseDTO response = mapper.toResponse(transaction);
        transactionRepository.deleteById(id);
        return response;
    }
}
