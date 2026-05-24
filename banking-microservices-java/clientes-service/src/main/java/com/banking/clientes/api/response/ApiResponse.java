package com.banking.clientes.api.response;

public record ApiResponse<T>(int status, T data) {}
