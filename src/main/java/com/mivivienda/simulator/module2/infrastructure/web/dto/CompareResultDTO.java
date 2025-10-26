package com.mivivienda.simulator.module2.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CompareResultDTO(
        UUID baselineId,
        List<Entry> entries
) {
    public record Entry(
            UUID id, String title,
            BigDecimal tcea, BigDecimal van, BigDecimal tir, BigDecimal monthlyPayment,
            BigDecimal deltaTceaPct, BigDecimal deltaVan, BigDecimal deltaTirPct, BigDecimal deltaMonthlyPayment
    ) {}
}
