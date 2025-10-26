package com.mivivienda.simulator.module2.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record SimulationSummaryDTO(
        UUID id,
        String title,
        OffsetDateTime createdAt,
        BigDecimal tcea,
        BigDecimal van,
        BigDecimal tir,
        BigDecimal monthlyPayment
) {}
