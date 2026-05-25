package com.banking.accounts.api.controller;

import com.banking.accounts.application.dto.ReportResponseDTO;
import com.banking.accounts.application.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Transaction report generation")
public class ReportsController {

    private final ReportService reportService;

    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    @Operation(summary = "Transaction report by client and date range")
    public ReportResponseDTO getReport(
            @RequestParam Integer clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return reportService.generate(clientId, startDate, endDate);
    }
}
