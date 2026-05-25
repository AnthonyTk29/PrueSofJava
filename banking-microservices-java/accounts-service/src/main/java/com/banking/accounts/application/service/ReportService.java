package com.banking.accounts.application.service;

import com.banking.accounts.application.dto.ReportResponseDTO;

import java.time.LocalDate;

public interface ReportService {

    ReportResponseDTO generate(Integer clientId, LocalDate startDate, LocalDate endDate);
}
