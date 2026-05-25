package com.banking.customers.api.controller;

import com.banking.customers.application.dto.CustomerCreateRequestDTO;
import com.banking.customers.application.dto.CustomerResponseDTO;
import com.banking.customers.application.dto.CustomerUpdateRequestDTO;
import com.banking.customers.application.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management")
public class CustomersController {

    private final CustomerService customerService;

    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "List all customers")
    public List<CustomerResponseDTO> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Get customer by ID")
    public CustomerResponseDTO getById(@PathVariable Integer clientId) {
        return customerService.getById(clientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new customer")
    public CustomerResponseDTO create(@Valid @RequestBody CustomerCreateRequestDTO request) {
        return customerService.create(request);
    }

    @PutMapping("/{clientId}")
    @Operation(summary = "Update customer")
    public CustomerResponseDTO update(
            @PathVariable Integer clientId,
            @Valid @RequestBody CustomerUpdateRequestDTO request) {
        return customerService.update(clientId, request);
    }

    @PatchMapping("/{clientId}/status")
    @Operation(summary = "Change customer status")
    public CustomerResponseDTO patchStatus(
            @PathVariable Integer clientId,
            @RequestParam boolean active) {
        return customerService.patchStatus(clientId, active);
    }

    @DeleteMapping("/{clientId}")
    @Operation(summary = "Delete customer")
    public CustomerResponseDTO delete(@PathVariable Integer clientId) {
        return customerService.delete(clientId);
    }
}
