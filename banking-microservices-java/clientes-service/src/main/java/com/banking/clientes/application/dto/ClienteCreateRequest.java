package com.banking.clientes.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteCreateRequest(
        @NotBlank(message = "El nombre es requerido") String nombre,
        @NotBlank(message = "El género es requerido") String genero,
        @Min(value = 0, message = "La edad no puede ser negativa") int edad,
        @NotBlank(message = "La identificación es requerida") String identificacion,
        @NotBlank(message = "La dirección es requerida") String direccion,
        @NotBlank(message = "El teléfono es requerido") String telefono,
        @NotBlank(message = "La contraseña es requerida") String contrasena,
        @NotNull boolean estado
) {}
