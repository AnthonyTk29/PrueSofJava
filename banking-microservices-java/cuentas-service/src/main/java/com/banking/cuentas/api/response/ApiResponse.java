package com.banking.cuentas.api.response;

public record ApiResponse<T>(int status, T data) {}
