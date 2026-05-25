package com.banking.accounts.api.controller;

import com.banking.accounts.application.dto.TransactionCreateRequestDTO;
import com.banking.accounts.application.dto.TransactionResponseDTO;
import com.banking.accounts.application.dto.TransactionUpdateRequestDTO;
import com.banking.accounts.application.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Bank transaction management")
public class TransactionsController {

    private final TransactionService transactionService;

    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @Operation(summary = "List all transactions")
    public List<TransactionResponseDTO> getAll() {
        return transactionService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public TransactionResponseDTO getById(@PathVariable Integer id) {
        return transactionService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new transaction")
    public TransactionResponseDTO create(@Valid @RequestBody TransactionCreateRequestDTO request) {
        return transactionService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction")
    public TransactionResponseDTO update(
            @PathVariable Integer id,
            @Valid @RequestBody TransactionUpdateRequestDTO request) {
        return transactionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction")
    public TransactionResponseDTO delete(@PathVariable Integer id) {
        return transactionService.delete(id);
    }
}
