package com.banking.clientes.application.dto;

public record ClienteResponse(
        Integer clienteId,
        String nombre,
        String genero,
        int edad,
        String identificacion,
        String direccion,
        String telefono,
        boolean estado
) {}
