package com.banking.accounts.api.controller;

import com.banking.accounts.application.dto.AccountCreateRequestDTO;
import com.banking.accounts.application.dto.AccountResponseDTO;
import com.banking.accounts.application.dto.AccountUpdateRequestDTO;
import com.banking.accounts.application.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Bank account management")
public class AccountsController {

    private final AccountService accountService;

    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @Operation(summary = "List all accounts")
    public List<AccountResponseDTO> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID")
    public AccountResponseDTO getById(@PathVariable Integer id) {
        return accountService.getById(id);
    }

    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Get account by number")
    public AccountResponseDTO getByAccountNumber(@PathVariable String accountNumber) {
        return accountService.getByAccountNumber(accountNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new account")
    public AccountResponseDTO create(@Valid @RequestBody AccountCreateRequestDTO request) {
        return accountService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account")
    public AccountResponseDTO update(
            @PathVariable Integer id,
            @Valid @RequestBody AccountUpdateRequestDTO request) {
        return accountService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change account status")
    public AccountResponseDTO patchStatus(
            @PathVariable Integer id,
            @RequestParam boolean active) {
        return accountService.patchStatus(id, active);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account")
    public AccountResponseDTO delete(@PathVariable Integer id) {
        return accountService.delete(id);
    }
}
