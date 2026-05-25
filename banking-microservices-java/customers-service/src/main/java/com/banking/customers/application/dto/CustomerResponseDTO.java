package com.banking.customers.application.dto;

public record CustomerResponseDTO(
        Integer clientId,
        String name,
        String gender,
        int age,
        String identification,
        String address,
        String phone,
        boolean active
) {}
