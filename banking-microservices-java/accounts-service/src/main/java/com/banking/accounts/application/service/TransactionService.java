package com.banking.accounts.application.service;

import com.banking.accounts.application.dto.TransactionCreateRequestDTO;
import com.banking.accounts.application.dto.TransactionResponseDTO;
import com.banking.accounts.application.dto.TransactionUpdateRequestDTO;

import java.util.List;

public interface TransactionService {

    List<TransactionResponseDTO> getAll();

    TransactionResponseDTO getById(Integer id);

    TransactionResponseDTO create(TransactionCreateRequestDTO request);

    TransactionResponseDTO update(Integer id, TransactionUpdateRequestDTO request);

    TransactionResponseDTO delete(Integer id);
}
