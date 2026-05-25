package com.banking.accounts.api.response;

public record ApiResponse<T>(int status, T data) {}
