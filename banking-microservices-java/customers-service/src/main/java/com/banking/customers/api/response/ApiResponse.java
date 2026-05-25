package com.banking.customers.api.response;

public record ApiResponse<T>(int status, T data) {}
