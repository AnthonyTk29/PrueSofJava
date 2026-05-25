package com.banking.customers.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerCreateRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String name,
        @NotBlank(message = "El género es obligatorio") String gender,
        @Min(value = 0, message = "La edad no puede ser negativa") int age,
        @NotBlank(message = "La identificación es obligatoria") String identification,
        @NotBlank(message = "La dirección es obligatoria") String address,
        @NotBlank(message = "El teléfono es obligatorio") String phone,
        @NotBlank(message = "La contraseña es obligatoria") String password,
        @NotNull boolean active
) {}
