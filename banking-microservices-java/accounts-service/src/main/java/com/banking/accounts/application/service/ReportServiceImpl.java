package com.banking.accounts.application.service;

import com.banking.accounts.application.dto.ReportResponseDTO;
import com.banking.accounts.domain.entity.Account;
import com.banking.accounts.domain.entity.CustomerReadModel;
import com.banking.accounts.domain.entity.Transaction;
import com.banking.accounts.domain.exception.ClientNotExistsException;
import com.banking.accounts.domain.repository.AccountRepository;
import com.banking.accounts.domain.repository.CustomerReadModelRepository;
import com.banking.accounts.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final CustomerReadModelRepository customerReadModelRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public ReportServiceImpl(CustomerReadModelRepository customerReadModelRepository,
                              AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.customerReadModelRepository = customerReadModelRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponseDTO generate(Integer clientId, LocalDate startDate, LocalDate endDate) {
        CustomerReadModel customer = customerReadModelRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotExistsException("Cliente con Id " + clientId + " no encontrado"));

        Instant start = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = endDate.atTime(23, 59, 59, 999_999_999).atOffset(ZoneOffset.UTC).toInstant();

        List<Account> accounts = accountRepository.findByClientId(clientId);

        List<ReportResponseDTO.AccountReport> accountReports = accounts.stream()
                .map(account -> {
                    List<Transaction> transactions = transactionRepository
                            .findByAccountIdAndDateBetween(account.getId(), start, end);

                    List<ReportResponseDTO.TransactionReport> transactionReports = transactions.stream()
                            .map(t -> new ReportResponseDTO.TransactionReport(
                                    t.getDate(), t.getTransactionType(), t.getAmount(), t.getBalance()))
                            .toList();

                    return new ReportResponseDTO.AccountReport(
                            account.getAccountNumber(),
                            account.getAccountType(),
                            account.getInitialBalance(),
                            account.getAvailableBalance(),
                            account.isActive(),
                            transactionReports
                    );
                })
                .toList();

        return new ReportResponseDTO(clientId, customer.getName(), start, end, accountReports);
    }
}
