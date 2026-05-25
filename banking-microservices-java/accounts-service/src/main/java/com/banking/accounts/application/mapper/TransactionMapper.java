package com.banking.accounts.application.mapper;

import com.banking.accounts.application.dto.TransactionResponseDTO;
import com.banking.accounts.domain.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDTO toResponse(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAccountNumber(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getBalance(),
                transaction.getDate()
        );
    }
}
